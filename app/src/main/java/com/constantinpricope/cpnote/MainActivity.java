package com.constantinpricope.cpnote;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    ListView lvRss;
    Button button;
    ArrayList<String> titles;
    ArrayList<String> links;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvRss = (ListView) findViewById(R.id.lvRss);
        button = (Button) findViewById(R.id.button);

        titles = new ArrayList<String>();
        links = new ArrayList<String>();

        lvRss.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(links.get(position));

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(v);
            }
        });



        ProcessInBackground processInBackground = new  ProcessInBackground();
        if(checkPermission(Manifest.permission.INTERNET,  android.os.Process.myPid(), android.os.Process.myUid()) == PackageManager.PERMISSION_GRANTED)
        {
            processInBackground.execute();
        }
        else
        {
            System.out.println("No permission!");
        }
    }
    public void sendMessage(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        String message = "First title in Rss Feed is:" +
                " " + titles.get(0);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public InputStream getInputStream(URL url)
    {
        try
        {
            URLConnection urlConnection = url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();

            return  inputStream;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    public class ProcessInBackground extends AsyncTask<Integer, Void, Exception >
    {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        Exception exception = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Start onPreExecute method...");
            progressDialog.setMessage("Se incarca Rss feed..va rugam sa asteptati...");
            progressDialog.show();
        }

        @Override
        protected Exception doInBackground(Integer... integers) {
            try
            {
                System.out.println("Start do in background method...");
//                URL url = new URL("https", "nasa.gov", "/rss/dyn/breaking_news.rss");
                URL url = new URL("https://www.nasa.gov/rss/dyn/breaking_news.rss");
                URLConnection urlConnection = null;
                InputStream inputStream = new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 0;
                    }
                };

                System.out.println("Url set try to get an open Conection");
                try
                {
                    System.out.println("Try to open a connection");
                    urlConnection = url.openConnection();
//                    urlConnection.connect();
                    String mimeType = urlConnection.getContentType( );
                    System.out.println(" The mime type is : "+mimeType);
                    System.out.println(" The time out time of connection is : "+urlConnection.getConnectTimeout());
                    try
                    {
                        System.out.println("Try to set InputStream.");
                        inputStream = urlConnection.getInputStream();
                    } catch (UnknownServiceException e)
                    {
                        System.out.println("Open InputStream failed. UnknownServiceException");
                    } catch (IOException e)
                    {
                        System.out.println("Open InputStream failed. IOException");
                    }
                }
                catch (IOException e)
                {
                    System.out.println("OpenConnection failed.");
                }

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();

                factory.setNamespaceAware(false);

                XmlPullParser xmlPullParser = factory.newPullParser();


//                if(getInputStream(url) != null)
//                {
//                    xmlPullParser.setInput(getInputStream(url), "UTF_8");
//                }
//                else
//                {
//                    System.out.println("T### hey ");
//                }

                xmlPullParser.setInput(inputStream, "UTF-8");

                boolean insideItem = false;

                int eventType = xmlPullParser.getEventType();
                System.out.println("Before while");

                int i = 1;
                while(eventType != XmlPullParser.END_DOCUMENT && i > 0)
                {
//                    i--;
                    System.out.println("current step is " + i);
                    if(eventType == XmlPullParser.START_TAG)
                    {
                        if(xmlPullParser.getName().equalsIgnoreCase("item"))
                        {
                            insideItem = true;
                        }
                        else if (xmlPullParser.getName().equalsIgnoreCase("title"))
                        {
                            if(insideItem)
                            {
                                titles.add(xmlPullParser.nextText());
                            }
                        }
                        else if(xmlPullParser.getName().equalsIgnoreCase("link"))
                        {
                            if(insideItem)
                            {
                                links.add(xmlPullParser.nextText());
                            }
                        }
                    } else if((eventType == XmlPullParser.END_TAG) &&
                            (xmlPullParser.getName().equalsIgnoreCase("item")))
                    {
                        insideItem = false;
                    }

                    eventType = xmlPullParser.next();
                }

            }
            catch (MalformedURLException e)
            {
                exception = e;
            }
            catch (XmlPullParserException e)
            {
                exception = e;
            }
            catch (IOException e)
            {
                exception = e;
            }


            return exception;
        }

        @Override
        protected void onPostExecute(Exception s) {
            super.onPostExecute(s);
            System.out.println("Start onPostExecute method...");

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, titles);

            lvRss.setAdapter(adapter);

            progressDialog.dismiss();
        }
    }
}
