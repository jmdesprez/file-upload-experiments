package jm.desprez.resources

import com.google.inject.Inject
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import jm.desprez.model.Hello
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import jm.desprez.manager.FileManager
import org.glassfish.jersey.media.multipart.FormDataContentDisposition
import org.glassfish.jersey.media.multipart.FormDataParam
import java.io.InputStream
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.core.Response


@Path("/")
@Api(value = "/", description = "Entry point.")
@Produces(MediaType.APPLICATION_JSON)
class DWResources @Inject constructor(val fileManager: FileManager) {

    @ApiOperation(value = "Say hello")
    @ApiResponses(value = ApiResponse(code = 200, message = ""))
    @GET
    fun sayHello(): Hello {
        return Hello("coucou")
    }

    @ApiOperation(value = "Send a file")
    @ApiResponses(value = ApiResponse(code = 200, message = ""))
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    fun uploadFile(
            @FormDataParam("name") fileName: String,
            @FormDataParam("file") uploadedInputStream: InputStream,
            @FormDataParam("file") fileDetail: FormDataContentDisposition): Response {

        fileManager.saveFile(uploadedInputStream, fileName)

        return Response.ok(Hello("File saved in $fileName")).build()
    }
}

