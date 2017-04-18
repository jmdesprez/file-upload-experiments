package jm.desprez

import com.google.inject.Injector
import com.google.inject.Module
import com.google.inject.Stage
import com.netflix.governator.guice.LifecycleInjector
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.forms.MultiPartBundle
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.federecio.dropwizard.swagger.SwaggerBundle
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration
import org.reflections.ReflectionUtils
import org.reflections.Reflections
import org.reflections.scanners.ResourcesScanner
import org.reflections.scanners.SubTypesScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import ru.vyarus.dropwizard.guice.GuiceBundle
import ru.vyarus.dropwizard.guice.injector.InjectorFactory


class GovernatorInjectorFactory : InjectorFactory {
    override fun createInjector(stage: Stage, modules: Iterable<Module>): Injector {
        return LifecycleInjector.builder().withModules(modules).inStage(stage).build().createInjector()
    }
}

fun <T : Configuration> Bootstrap<T>.addGuiceBundle(vararg packagesToScan: String, printDiagnostic: Boolean = false) {
    addGuiceBundle(packagesToScan.toList(), printDiagnostic)
}

fun <T : Configuration> Bootstrap<T>.addGuiceBundle(packagesToScan: List<String>, printDiagnostic: Boolean = false) {
    val reflections = Reflections(ConfigurationBuilder().setUrls(ClasspathHelper.forPackage("jm.desprez"))
                                          .setScanners(SubTypesScanner(true)))
    val modules = reflections.getSubTypesOf(Module::class.java).filter { subType ->
        val packageName = subType.`package`.name
        packagesToScan.any { packageName.startsWith(it) }
    }.map { it.kotlin }.map { klass ->
        klass.objectInstance ?: klass.constructors.find {
            it.parameters.isEmpty()
        }?.call()
    }.filter { it != null }

    addBundle(GuiceBundle.builder<T>()
                      .injectorFactory(GovernatorInjectorFactory())
                      .enableAutoConfig(*packagesToScan.toTypedArray())
                      .modules(*modules.toTypedArray())
                      .let { if (printDiagnostic) it.printDiagnosticInfo() else it }
                      //.modules(DWModule())
                      //.printDiagnosticInfo()
                      .build())
}

class DWApplication : Application<DWConfiguration>() {

    override fun initialize(bootstrap: Bootstrap<DWConfiguration>?) {
        super.initialize(bootstrap)

        bootstrap?.apply {
            addBundle(object : SwaggerBundle<DWConfiguration>() {
                override fun getSwaggerBundleConfiguration(configuration: DWConfiguration): SwaggerBundleConfiguration {
                    return configuration.swagger
                }
            })

            addGuiceBundle("jm.desprez")

            addBundle(MultiPartBundle())
        }

    }

    override fun run(configuration: DWConfiguration?, environment: Environment?) {

    }
}

fun main(args: Array<String>) {
    DWApplication().run(*args)
}