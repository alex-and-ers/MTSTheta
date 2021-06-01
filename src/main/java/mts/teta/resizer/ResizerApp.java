package mts.teta.resizer;

import mts.teta.resizer.imageprocessor.*;
import picocli.CommandLine;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


    class ConsoleAttributes
    {
        @CommandLine.Option(names = "--resize", split = ",", description = "Changes size of the image. 2 parameters: width, height.")
        int[] resizeOption;

        @CommandLine.Option(names = "--quality", description = "Changes quality value of the image. 1 parameter")
        double qualityOption;

        @CommandLine.Option(names = "--crop", split = ",", description = "Croppes a part of the image. 4 parameters: y x width height")
        int[] cropOption;

        @CommandLine.Option(names = "--blur", description = "Blures the image. 1 parameter: blur radius")
        int blurOption;

        @CommandLine.Option(names = "--format", description = "Changes format. 1 parameter: outputFormat")
        String formatOption;

        @CommandLine.Parameters(description = "Contains input and output files")
        String[] fileParameters;


    }

@CommandLine.Command(name = "resizer", mixinStandardHelpOptions = true, sortOptions = false,
        headerHeading = "Version: 1.0 %n", synopsisHeading = "Usage: convert input-file [options ...] output-file %n", header = "Available formats: jpg, png %n",
        version = "resizer 0.0.1", description = "Option settings: %n")
public class ResizerApp extends ConsoleAttributes implements Callable<Integer>
{

    File inputFile;

    File outputFile;

    public static void main(String... args)
    {
        int exitCode = runConsole(args);
        System.exit(exitCode);
    }

    protected static int runConsole(String[] args)
    {
        return new CommandLine(new ResizerApp()).execute(args);
    }

    @Override
    public Integer call() throws Exception
    {
        if (fileParameters != null)
        {
            if (fileParameters.length == 2 && !(fileParameters[0] == null))
            {
                setInputFile(new File(fileParameters[0]));
            }
        }

        if(inputFile != null)
        {
            ImageProcessor imageProcessor = new ImageProcessor();
            imageProcessor.processImage(ImageIO.read(inputFile), this);
            return 0;
        }
        else
        {
            throw new IIOException("Can't read input file!");
        }
    }

    void setInputFile(File file)
    {
        if (file.exists())
        {
            this.inputFile = file;

            if (fileParameters == null)
            {
                fileParameters = new String[2];
            }

            fileParameters[0] = file.getName();
        }
    }

    void setOutputFile(File file)
    {
        this.outputFile = file;

        if (fileParameters == null)
        {
            fileParameters = new String[2];
        }

        fileParameters[1] = file.getName();
    }

    void setTargetWidth(int targetWidth)
    {
        if (resizeOption == null)
        {
            resizeOption = new int[2];
        }

        resizeOption[0] = targetWidth;
    }

    void setTargetHeight(int targetHeight)
    {
        if (resizeOption == null)
        {
            resizeOption = new int[2];
        }

        resizeOption[1] = targetHeight;
    }

    void setQuality(double quality)
    {
        qualityOption = quality;
    }

    void setBlurRadius(int blurRadius)
    {
        blurOption = blurRadius;
    }

    void setCrop(int width, int height, int x, int y)
    {
        if (cropOption == null)
        {
            cropOption = new int[4];
        }
        cropOption[0] = width;
        cropOption[1] = height;
        cropOption[2] = x;
        cropOption[3] = y;
    }

    void setFormat(String format)
    {
        formatOption = format;
    }

}

    class ImageProcessor extends ResizerApp
    {
        List<Options> option;

        void processImage(BufferedImage image, ResizerApp resizerApp) throws BadAttributesException, IOException
        {
            optionValidator(resizerApp);

            if (option.size() >= 2)
            {
                if (option.get(0).equals(Options.RESIZE) && option.get(1).equals(Options.QUALITY))
                {
                    Resizer resizer = new Resizer(resizerApp.resizeOption, resizerApp.inputFile, resizerApp.outputFile, resizerApp.fileParameters, resizerApp.qualityOption);
                    resizer.changeSizeAndQuality();
                }
                else
                {
                    throw new BadAttributesException("You can use only one option per command!");
                }
            }
            else {
                Options result = option.get(0);

                switch (result)
                {
                    case RESIZE: Resizer resizer = new Resizer(resizerApp.resizeOption, resizerApp.inputFile, resizerApp.outputFile, resizerApp.fileParameters);
                    resizer.changeSize();
                    break;
                    case QUALITY: Resizer resizerQuality = new Resizer(resizerApp.inputFile, resizerApp.outputFile, resizerApp.fileParameters, resizerApp.qualityOption, image);
                    resizerQuality.changeQuality();
                    break;
                    case CROP: Cropper cropper = new Cropper(resizerApp.cropOption, resizerApp.inputFile, resizerApp.outputFile, resizerApp.fileParameters, image);
                    cropper.cropImage();
                    break;
                    case BLUR: Blurrer blurrer = new Blurrer(resizerApp.inputFile, resizerApp.outputFile, resizerApp.fileParameters, resizerApp.blurOption);
                    blurrer.blurImage();
                    break;
                    case FORMAT: Formatter formatter = new Formatter(resizerApp.inputFile, resizerApp.outputFile, resizerApp.fileParameters, resizerApp.formatOption, image);
                    formatter.formatImage();
                    break;
                    default: throw new BadAttributesException("Please check params!");
                }
            }
        }

        private void optionValidator(ResizerApp app)
        {
            option = new ArrayList<>();

            if (app.resizeOption != null)
            {
                option.add(Options.RESIZE);
            }
            if (app.qualityOption > 0.0 && app.qualityOption <= 1.0)
            {
                option.add(Options.QUALITY);
            }
            if (app.cropOption != null)
            {
                option.add(Options.CROP);
            }
            if (app.blurOption > 0)
            {
                option.add(Options.BLUR);
            }
            if (app.formatOption != null)
            {
                option.add(Options.FORMAT);
            }
            if (option.isEmpty())
            {
                option.add(Options.UNKNOWN);
            }
        }
    }
