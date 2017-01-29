package com.ealchemy.media.video;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kirk on 1/28/17.
 */
public class VideoPreviewGenerator {

    private String guid;
    private String imageFileName;

    public static void main(String[] args) {
        VideoPreviewGenerator gen = new VideoPreviewGenerator();
        gen.generatateImagePreview(new File ("test.mp4"));

    }

    public void fromURL(String url){
        URL theURL = null;
        try {
            theURL = new URL(url);
            String videoFile = getVideoFromURL(theURL);
            File file = new File(videoFile);
            generatateImagePreview(file);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private String getVideoFromURL(URL url){
        String fileName = "";
        try {
            // get URL content
            URLConnection conn = url.openConnection();
            InputStream input = conn.getInputStream();

            //save to this filename
            fileName = FilenameUtils.getName(url.getPath());
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }

            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            byte[] buffer = new byte[4096];
            int n = - 1;

            OutputStream output = new FileOutputStream( file );
            while ( (n = input.read(buffer)) != -1)
            {
                out.write(buffer, 0, n);
            }
            out.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileName;

    }

    public void generatateImagePreview(File file){
        exeFFMPEGGenerateImage(file);
    }

    private String exeFFMPEGGenerateImage(File file){
        String ffmpegResult = null;
        String imageFileName=FilenameUtils.getBaseName(file.getAbsolutePath()) + "_preview.jpg";
        ProcessBuilder pb = new ProcessBuilder("bin/video_preview.sh", file.getAbsolutePath(), "120", "100", "1", imageFileName);
        try {
            Process p = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ffmpegResult;
    }

    private BufferedImage getPreviewImage(String filePath){
        BufferedImage img = null;
        try {
            File imageFile = new File(filePath);
            img = ImageIO.read(imageFile);
            imageFile.deleteOnExit();
        } catch (IOException e) {
        }

        return img;
    }
}
