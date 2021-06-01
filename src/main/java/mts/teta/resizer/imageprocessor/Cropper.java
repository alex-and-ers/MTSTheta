package mts.teta.resizer.imageprocessor;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static marvinplugins.MarvinPluginCollection.crop;

public class Cropper
{
    private int[] cropParams;

    private File inputFile;

    private File outputFile;

    private String[] fileParams;

    private BufferedImage image;

    public Cropper(int[] cropParams, File inputFile, File outputFile, String[] fileParams, BufferedImage image)
    {
        this.cropParams = cropParams;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.fileParams = fileParams;
        this.image = image;
    }

    private void argsValidator() throws BadAttributesException
    {
        if (fileParams.length != 2 || fileParams[0].isEmpty() || fileParams[1].isEmpty())
        {
            throw new BadAttributesException("Parameter error! Invalid file parameters");
        }
        if (inputFile == null || !inputFile.exists())
        {
            throw new BadAttributesException("Parameter error! The input file you typed doesn't exist");
        }
        if (outputFile == null || !outputFile.exists())
        {
            if (fileParams[1] == null)
            {
                String fileName = inputFile.getName();

                if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
                {
                    fileParams[1] = inputFile.getName().substring(0, fileName.lastIndexOf("."))
                            + ".preview." + fileName.substring(fileName.lastIndexOf(".") + 1);
                }
            }
            outputFile = new File(fileParams[1]);
        }
        if (cropParams.length != 4 || cropParams[0] < 0 || cropParams[0] >= image.getWidth()
                || cropParams[1] < 0 || cropParams[1] >= image.getHeight())
        {
            throw new BadAttributesException("Parameter error! Invalid crop parameters");
        }
        if (cropParams[2] <= 0 || cropParams[3] <= 0)
        {
            throw new BadAttributesException("Parameter error! Invalid crop parameters");
        }
    }

    public void cropImage() throws IOException, BadAttributesException
    {
        argsValidator();

        MarvinImage image = MarvinImageIO.loadImage(inputFile.getAbsolutePath());
        crop(image.clone(), image, cropParams[0], cropParams[1], cropParams[2], cropParams[3]);
        MarvinImageIO.saveImage(image, outputFile.getAbsolutePath());
    }
}
