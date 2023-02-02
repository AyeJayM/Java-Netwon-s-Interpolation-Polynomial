// Austin Martinez
// CS 3010
// Assignment 4

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class newtonPoly 
{
    public static void main(String[] args) throws IOException
    {

        String filename = "Placeholder";
        boolean Exercise3 = false; // These bools determine if we use an n version
        boolean Exercise4 = false;
        int n = 0; // Placeholder


        ///////////////////////
        // READ COMMAND LINE //
        ///////////////////////


        // N was given. Exercise 4
        if (args.length == 2)
        {
            System.out.println("\nInvalid Comand Line Input.\n\n" +
            "Command Line Input Guide:\n\n" +
            "For Exercise 3 (No n Provided) : \" poly.pnt \"\n" +
            "For Exercise 4 (n Provided)    : \" 5 \"\n");
        }
        else if ( (checkInteger(args[0]) == true) )
        {
            n = Integer.parseInt(args[0]);
            Exercise4 = true;
        }
        else if ( checkInteger(args[0]) == false ) // NO n given. Exercise 3.
        {
            filename = args[0];
            Exercise3 = true;
        }
      

        ///////////////////////////////////////
        // READ FROM FILE LOGIC / EXERCISE 3 //
        ///////////////////////////////////////

        List<Double> xsAL = new ArrayList<>();
        List<Double> ysAL = new ArrayList<>();

        String line1 = "Placeholder";
        String line2 = "Placeholder";

        if (Exercise3 == true)
        {
            FileReader file = new FileReader (filename);

            BufferedReader buffer = new BufferedReader(file);

            line1 = buffer.readLine();

            line2 = buffer.readLine();

            Matcher matcher = Pattern.compile( "[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?" ).matcher(line1); // Read in X's

            while (matcher.find() )
            {
                double foundDub = Double.parseDouble(matcher.group() );
                xsAL.add(foundDub);
            }

            Matcher matcher2 = Pattern.compile( "[-+]?\\d*\\.?\\d+([eE][-+]?\\d+)?" ).matcher(line2); // Read in Y's

            while (matcher2.find() )
            {
                double foundDub2 = Double.parseDouble(matcher2.group());
                ysAL.add(foundDub2);
            }

            buffer.close(); // Make sure we don't leak resources

        }






        //////////////////////////////////
        // GENERATE POINTS / EXERCISE 4 //
        //////////////////////////////////

        if(Exercise4 == true)
        {
            double min = 0;
            double max = Math.pow(10, n);

            Random random = new Random();
    
            double[] randX = new double[n+1];
            double[] randY = new double[n+1];

            for(int i = 0; i < n; i++)
            {
                double ranVal = min + (max - min) * random.nextDouble();
                randX[i] = ranVal;
            }

            for(int i = 0; i < n; i++)
            {
                double ranVal = min + (max-min) * random.nextDouble();
                randY[i] = ranVal;
            }

            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("randomFunction.pnt"), "utf-8"))) 
                {
                    writer.write(Arrays.toString(randX));
                    writer.write("\n");
                    writer.write(Arrays.toString(randY));
                }

            System.out.println("\nYour randomly-generated dataset has been outputted to a file " +
            "titled \"randomFunction.pnt\".");

            randInterpolation(randX, randY);

        }







        ////////////////////////////////////////////
        //  ASSIGN AL TO ARRAYS & CREATE CS ARRAY //
        ////////////////////////////////////////////


        double[] xs = new double[xsAL.size()+1]; // Convert x AL to an array

        for(int i = 0; i < xsAL.size(); i++)
        {
            xs[i] = xsAL.get(i);
        }

        double[] ys = new double[ysAL.size()+1]; // Convert y AL to an array

        for(int i = 0; i < ysAL.size(); i++)
        {
            ys[i] = ysAL.get(i);
        }

        System.out.println("\nYour X Array:"); // Print just to verify correctly read
        for (int i = 0; i < xs.length; i++)
        {
            System.out.print(xs[i] + " ");
        }

        System.out.println("\n\nYour Y Array:"); // Print just to verify correctly read
        for (int i = 0; i < ys.length; i++)
        {
            System.out.print(ys[i] + " ");
        }


        /////////////////////
        // CREATE CS ARRAY //
        /////////////////////
        
        
        double[] cs = new double[ys.length];

        Coeff(xs, ys, cs);

        System.out.println("\n\nYour Coefficient Array:"); // Print just to verify correctly read
        for (int i = 0; i < cs.length-1; i++)
        {
            System.out.print(cs[i] + " ");
        }


        ////////////////////////////
        // HERE IS WHERE WE GET Z //
        ////////////////////////////

        Scanner scanner = new Scanner (System.in);

        String choice = "Placeholder";

        while (choice.equals("q") == false || choice.equals("Q") == false)
        {
            System.out.println("\n\nPlease enter a value to be used to evaluate the " +
        "polynomial. If you wish to quit, enter \"q\".");

            choice = scanner.next();

            if(choice.equals("q") == true || choice.equals("Q") == true)
            {
                System.out.println("\nEnding program...");
                break;
            }

            double z = Double.parseDouble(choice);

            double solution = EvalNewton(xs, cs, z);

            System.out.println("\n\nThe solution is: \n"
                + solution);

        }

        scanner.close();
        
    }


    ////////////////////////////
    // NEWTON'S INTERPOLATION //
    ////////////////////////////

    public static void Coeff(double[] xs, double[] ys, double[] cs)
    {
        for (int i = 0; i < xs.length-1; i++)
        {
            cs[i] = ys[i];
        }

        for (int j = 1; j < ys.length-1; j++)
        {
            for (int i = ys.length-2; i >= j; i--)
            {
                cs[i] =  (cs[i] - cs[i-1]) / (xs[i] - xs[i-j]);
            }
        }
        
    }

    public static double EvalNewton(double[] xs, double[] cs, double z)
    {
        double result = cs[cs.length-2];

        for(int i = cs.length - 3; i >= 0; i--)
        {
            result = result * (z - xs[i]) + cs[i];
        }
        
        return result;
    }


    ////////////////////////////////////////////////
    // USED TO CHECK IF DOUBLE WAS INPUTTED FOR N //
    ////////////////////////////////////////////////

    public static boolean checkInteger(String checkedString)
    {
        try
        {
            Integer.parseInt(checkedString);
        }
        catch (NumberFormatException e) 
        {
            return false;
        }

        return true;
    }
    

    //////////////////////////
    // RANDOM INTERPOLATION //
    //////////////////////////

    public static void randInterpolation(double[] randX, double[] randY)
    {
        double[] randC = new double[randX.length];

        long CoeffStart = System.nanoTime(); // START

        Coeff(randX, randY, randC);

        long CoeffStop = System.nanoTime(); // STOP

        long CoeffElapsed = CoeffStop - CoeffStart;

        System.out.println("\nTime taken for Coeff function using randomly-generated data points: \n"
        + CoeffElapsed + " nanoseconds.");


        Scanner scanner = new Scanner (System.in);

        String choice = "Placeholder";

        while (choice.equals("q") == false || choice.equals("Q") == false)
        {
            System.out.println("\n\nPlease enter a value to be used to evaluate the" +
        " polynomial interpolated from the randomly-generated values. If you wish to quit, enter \"q\".");

            choice = scanner.next();

            if(choice.equals("q") == true || choice.equals("Q") == true)
            {
                System.out.println("\nEnding program...");
                break;
            }

            double z = Double.parseDouble(choice);

            long startTime = System.nanoTime(); // START STOPWATCH

            double solution = EvalNewton(randX, randC, z);

            long stopTime = System.nanoTime();  // STOP STOPWATCH

            long elapsedTime = stopTime - startTime; // MEASURED TIME

            System.out.println("\n\nThe solution is: \n"
                + solution);

            System.out.println("\nTime taken to solve the polynomial for"
            + " provided point:\n" + elapsedTime + " nanoseconds.");

        }

        scanner.close();
        System.exit(0);

    }
}
