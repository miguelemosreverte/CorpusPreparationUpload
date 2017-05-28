package controllers;

import play.data.Form;
import play.mvc.*;
import views.html.index;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
        System.out.println(LM_name);
        return ok("Success!");
    }
}

