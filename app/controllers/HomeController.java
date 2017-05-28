package controllers;

import play.data.Form;
import play.mvc.*;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * This class uses a custom body parser to change the upload type.
 */
@Singleton
public class HomeController extends Controller {

    private final play.data.FormFactory formFactory;

    @Inject
    public HomeController(play.data.FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    public Result index() {
        Form<FormData> form = formFactory.form(FormData.class);
        Http.Context context = Http.Context.current();
        return ok(index.render(form, context.messages()));
    }

    @BodyParser.Of(MyMultipartFormDataBodyParser.class)
    public Result upload() throws IOException {
        final Http.MultipartFormData<File> formData = request().body().asMultipartFormData();
        final String LM_name = request().body().asMultipartFormData().asFormUrlEncoded().get("LM_name")[0].toString();
        final Http.MultipartFormData.FilePart<File> CorpusPreparationSource_filePart = formData.getFile("CorpusPreparationSource");
        final Http.MultipartFormData.FilePart<File> CorpusPreparationTarget_filePart = formData.getFile("CorpusPreparationTarget");
        final Http.MultipartFormData.FilePart<File> CorpusPreparationLM_filePart = formData.getFile("CorpusPreparationLM");
        final File CorpusPreparationSource = CorpusPreparationSource_filePart.getFile();
        final File CorpusPreparationTarget = CorpusPreparationTarget_filePart.getFile();
        final File CorpusPreparationLM = CorpusPreparationLM_filePart.getFile();

        String final_dir = "./"+LM_name+"/";

        File theDir = new File(final_dir);
        if (!theDir.exists()) {
            theDir.mkdir();
        }

        String CorpusPreparationSource_filePath = final_dir+"CorpusPreparationSource";
        String CorpusPreparationTarget_filePath = final_dir+"CorpusPreparationTarget";
        String CorpusPreparationLM_filePath = final_dir+"CorpusPreparationLM";
        Files.deleteIfExists(Paths.get(CorpusPreparationSource_filePath));
        Files.deleteIfExists(Paths.get(CorpusPreparationTarget_filePath));
        Files.deleteIfExists(Paths.get(CorpusPreparationLM_filePath));

        CorpusPreparationSource.renameTo(new File(CorpusPreparationSource_filePath));
        CorpusPreparationTarget.renameTo(new File(CorpusPreparationTarget_filePath));
        CorpusPreparationLM.renameTo(new File(CorpusPreparationLM_filePath));


        return ok("Success!");
    }


    private long operateOnTempFile(String final_dir, String final_name, File file) throws IOException {
        System.out.println(final_name);
        final long size = Files.size(file.toPath());
        Files.deleteIfExists(Paths.get(final_dir+final_name));
        File theDir = new File(final_dir);
        if (!theDir.exists()) {
            theDir.mkdir();
        }
        if(file.renameTo(new File(final_dir + final_name))){
            System.out.println("File is moved successful!");
        }else{
            System.out.println("File is failed to move!" + final_dir + file.getName() );
        }
        return size;
    }
}

