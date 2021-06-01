package mts.teta.resizer.imageprocessor;

import marvin.image.MarvinImage;
import marvin.io.MarvinImageIO;

import java.io.File;
import java.io.IOException;

import static marvinplugins.MarvinPluginCollection.gaussianBlur;

public class Blurrer
{
    private File inputFile;

    private File outputFile;

    private String[] fileParams;

    private int blurRadius;

    public Blurrer(File inputFile, File outputFile, String[] fileParams, int blurRadius)
    {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.fileParams = fileParams;
        this.blurRadius = blurRadius;
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
        if (blurRadius < 0)
        {
            throw new BadAttributesException("Parameter error! Invalid blur Radius");
        }
    }

    public void blurImage() throws IOException, BadAttributesException
    {
        argsValidator();

        MarvinImage image = MarvinImageIO.loadImage(inputFile.getAbsolutePath());
        gaussianBlur(image.clone(), image, blurRadius);
        MarvinImageIO.saveImage(image, outputFile.getAbsolutePath());
    }
}
