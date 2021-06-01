package mts.teta.resizer.imageprocessor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Formatter
{
    private File inputFile;

    private File outputFile;

    private String[] fileParams;

    private String format;

    private BufferedImage image;

    public Formatter(File inputFile, File outputFile, String[] fileParams, String format, BufferedImage image)
    {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.fileParams = fileParams;
        this.format = format;
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
    }

    public void formatImage() throws IOException, BadAttributesException
    {
        argsValidator();


        String filename = cutFileExtension(outputFile);

        if (filename.equals(""))
        {
            throw new BadAttributesException("Parameter error! Invalid filename");
        }
        else if (format.equalsIgnoreCase("jpg") || format.equalsIgnoreCase("jpeg"))
        {
            ImageIO.write(image, "JPG", new File(filename + ".jpg"));
        }
        else if (format.equalsIgnoreCase("png"))
        {
            ImageIO.write(image, "PNG", new File(filename + ".png"));
        }
        else
        {
            throw new BadAttributesException("Parameter error! Invalid image format");
        }
    }

    private String cutFileExtension(File file)
    {
        String fileName = file.getName();

        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
        {
            return fileName.substring(0, fileName.lastIndexOf("."));
        }
        else return "";
    }
}
