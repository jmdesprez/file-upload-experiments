package jm.desprez

import com.google.inject.Provides
import jm.desprez.manager.FileManager
import ru.vyarus.dropwizard.guice.module.support.DropwizardAwareModule
import java.io.File

object DWModule : DropwizardAwareModule<DWConfiguration>() {

    override fun configure() {
    }

    @Provides
    fun provideFileManager() = FileManager(File(configuration().rootPath))
}

