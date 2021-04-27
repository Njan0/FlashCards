package njan.flashcards.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileNotFoundException;
import java.io.InputStream;

import njan.flashcards.CardSet;
import njan.flashcards.manager.CardSetManager;
import njan.flashcards.R;

public class ImportActivity extends AppCompatActivity {
    ActivityResultLauncher<String> launcherImportFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        launcherImportFile = registerForActivityResult(new ActivityResultContracts.GetContent(), this::importFile);
    }

    /**
     * Read xml file and add it to the card set.
     * @param uri uri of file
     */
    private void importFile(Uri uri) {
        if (uri == null)
            return;

        try {
            InputStream is = getContentResolver().openInputStream(uri);
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(is, null);
            CardSet cs = new CardSet(parser);
            if (!cs.isEmpty()) {
                CardSetManager.getInstance(this).addSet(cs);
                Snackbar.make(findViewById(R.id.constraintLayout), "Imported " + cs.getName(), Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(findViewById(R.id.constraintLayout), "Import failed", Snackbar.LENGTH_SHORT).show();
            }
        } catch (FileNotFoundException | XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    public void onClickImportFile(View view) {
        launcherImportFile.launch("*/*");
    }
}