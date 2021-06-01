package mts.teta.resizer.imageprocessor;

import net.coobird.thumbnailator.Thumbnails;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Resizer
{
    private int[] size;

    private File inputFile;

    private File outputFile;

    private String[] fileParams;

    private double quality;

    private BufferedImage image;

    public Resizer(int[] size, File inputFile, File outputFile, String[] fileParams)
    {
        this.size = size;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.fileParams = fileParams;
        this.quality = 1.0;
    }

    public Resizer(File inputFile, File outputFile, String[] fileParams, double quality, BufferedImage image)
    {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.fileParams = fileParams;
        this.quality = quality;
        this.image = image;
        this.size = new int[] {image.getWidth(), image.getHeight()};
    }

    public Resizer(int[] size, File inputFile, File outputFile, String[] fileParams, double quality)
    {
        this.size = size;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.fileParams = fileParams;
        this.quality = quality;
    }

    private void argsValidator() throws IOException, BadAttributesException
    {
        if (size.length != 2)
        {
            throw new BadAttributesException("Parameter error! There must be only two parameters");
        }
        if (size[0] <= 0 || size[1] <= 0)
        {
            throw new BadAttributesException("Parameter error! Parameter must be > 0 and < " + Integer.MAX_VALUE);
        }
        if (inputFile == null || !inputFile.exists())
        {
            throw new BadAttributesException("Parameter error! Invalid input file");
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
        if (quality <= 0.0 || quality > 1.0)
        {
            throw new BadAttributesException("Parameter error! Quality parameter must be > 0.0 and <= 100");
        }
    }

    public void changeSize() throws IOException, BadAttributesException
    {
        argsValidator();

        Thumbnails.of(inputFile)
                .size(size[0], size[1])
                .keepAspectRatio(false)
                .toFile(outputFile);
    }

    public void changeQuality() throws IOException, BadAttributesException
    {
        argsValidator();

        Thumbnails.of(inputFile)
                .size(size[0], size[1])
                .keepAspectRatio(false)
                .outputQuality(quality)
                .toFile(outputFile);
    }

    public void changeSizeAndQuality() throws IOException, BadAttributesException
    {
        argsValidator();

        Thumbnails.of(inputFile)
                .size(size[0], size[1])
                .keepAspectRatio(false)
                .outputQuality(quality)
                .toFile(outputFile);
    }
}
