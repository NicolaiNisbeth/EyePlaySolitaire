package cv.communication;


import java.io.*;
import java.net.URISyntaxException;


public class ClientStarter {

    // Relative path from the execution
    private static String ClIENT_PATH = "ComputerVision/source/main.py";

    private OutputListener standardOutputListener;
    private OutputListener errorOutputListener;

    private ProcessFinishedCallback processFinishedCallback;
    private Process process;

    /**
     * Starts the ComputerVision client program as a seperate process
     * using Python.
     * A seperate thread will be started to wait for the process to
     * finish.
     *
     * @param port The port number the client should connect to
     */
    public void start(int port) throws IOException, IllegalStateException {

        // Check python installation
        boolean pythonExists = checkPythonInstallation();
        if( !pythonExists )
            throw new IllegalStateException("Cannot find python installation");

        // Run the process
        String arguments[] = {
                "python",
                "-u", // Makes stdout unbuffered
                getClientPath(),
                Integer.toString(port)
        };
        process = Runtime.getRuntime().exec(arguments);
        new AsyncInputReader(process.getInputStream(), standardOutputListener);
        new AsyncInputReader(process.getErrorStream(), errorOutputListener);

        // Wait for process to finish on seperate thread
        new Thread(() -> {
            boolean processFinished = false;
            while(!processFinished)
                try{
                    process.waitFor(); // Blocks the thread until process is finished
                    processFinished = true;
                }catch(InterruptedException e){}

            if( processFinishedCallback != null )
                processFinishedCallback.onClientFinish(process.exitValue());
        }).start();
    }


    // Checks if Python is installed by running python -V, and
    // checking the exit code (should be 0)
    private static boolean checkPythonInstallation() throws IOException{
        Process process = Runtime.getRuntime().exec("python -V");

        // Ensures the process is waited for (even if thread is interrupted)
        boolean processFinished = false;
        while(!processFinished)
            try{
                process.waitFor(); // Blocks the thread until process is finished
                processFinished = true;
            }catch(InterruptedException e){}

        return process.exitValue() == 0;
    }


    /*  Builds/gets the path for the Client executable file */
    private static String getClientPath (){
        try {
            File file = new File(ClientStarter.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            // Check if .jar
            String path = file.getPath();
            if( path.length() > 4 && path.substring(path.length()-4).equals(".jar") )
                return file.getParentFile().getPath() + "/" + ClIENT_PATH;
            else
                return file.getParentFile().getParentFile() + "/" + ClIENT_PATH;
        } catch (URISyntaxException e) {
            System.out.println("Unexpected exception (shouldn't be able to happen):");
            e.printStackTrace();
        }
        return null;
    }

    public void stop(){
        if( process.isAlive() )
            process.destroyForcibly();
    }


    public void setStandardOutputListener(OutputListener standardOutputListener) {
        this.standardOutputListener = standardOutputListener;
    }

    public void setProcessFinishedCallback(ProcessFinishedCallback processFinishedCallback) {
        this.processFinishedCallback = processFinishedCallback;
    }

    public void setErrorOutputListener(OutputListener errorOutputListener) {
        this.errorOutputListener = errorOutputListener;
    }

    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    // Functional interfaces

    public interface OutputListener{
        void onClientOutput(String output);
    }

    public interface ProcessFinishedCallback{
        void onClientFinish(int exitCode);
    }


    // ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
    /* Reads an input stream on a seperate thread */
    private static class AsyncInputReader {
        private BufferedReader reader;
        private OutputListener listener;
        private Thread thread;

        /**
         * @param stream The inputstream to read from
         * @param listener The listener to be notified when a line has been read
         */
        AsyncInputReader(InputStream stream, OutputListener listener ){
            this.listener = listener;

            reader = new BufferedReader(new InputStreamReader(stream));

            thread = new Thread(this::readOutput);
            thread.start();
        }


        private void readOutput(){
            try{
                String input;
                while( (input=reader.readLine()) != null){
                    if( listener != null )
                        listener.onClientOutput(input);
                }
            }catch(IOException e){
                System.out.println("Unexpected exception occured when reading input stream");
                e.printStackTrace();
            }
        }
    }
}
