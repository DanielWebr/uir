import Classificators.BayesClassificator;
import Classificators.Classificator;
import Classificators.Custom1Classificator;
import Parametrizators.Custom1Parametrizator;
import Parametrizators.Custom2Parametrizator;
import Parametrizators.Custom3Parametrizator;
import Parametrizators.Parametrizator;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        boolean trainMode = args.length == 6;
        boolean classificationsMode = args.length == 1;
        if(!trainMode && !classificationsMode){
            System.out.print("Wrong number of arguments");
            return;
        }
        if(trainMode){
            File classesFile = new File(args[0]) ;
            File trainDataFolder = new File(args[1]);
            File testDataFolder = new File(args[2]);

            if(!classesFile.exists() ||  !trainDataFolder.exists()  || !testDataFolder.exists()){
                System.out.print("Files or file does not exist");
                return;
            }

            ClassificatorType classificatorType;
            ParametrizatorType parametrizatorType;
            try {
                parametrizatorType = ParametrizatorType.valueOf(args[3].toUpperCase());
                classificatorType = ClassificatorType.valueOf(args[4].toUpperCase());
            } catch (IllegalArgumentException e){
                System.out.print("Wrong classificator or parametrizator name");
                return;
            }

            String model = args[5];

            ArrayList<String> classes = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(classesFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    classes.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            File[] trainDataFiles = trainDataFolder.listFiles();
            File[] testDataFiles = testDataFolder.listFiles();

            if(trainDataFiles == null || testDataFiles == null){
                System.out.print("Wrong test or train data");
                return;
            }

            ArrayList<Data> trainData = getDataFromFiles(trainDataFiles);
            ArrayList<Data> testData = getDataFromFiles(testDataFiles);

            train(classes,trainData,testData,classificatorType,parametrizatorType,model);

        }

        if(classificationsMode){
            String model = args[0];
            classify(model);
        }
    }

    public static void train(ArrayList<String> classes, ArrayList<Data> trainData, ArrayList<Data> testData, ClassificatorType classificatorType, ParametrizatorType parametrizatorType, String modelName)  {
        Classificator classificator;
        Parametrizator parametrizator;

        switch(classificatorType){
            case BAYES:{
                classificator = new BayesClassificator();
                break;
            }
            case CUSTOM1:{
                classificator = new Custom1Classificator();
                break;
            }
            default:return;
        }
        switch(parametrizatorType){
            case CUSTOM1:{
                parametrizator = new Custom1Parametrizator();
                break;
            }
            case CUSTOM2:{
                parametrizator = new Custom2Parametrizator();
                break;
            }
            case CUSTOM3:{
                parametrizator = new Custom3Parametrizator();
                break;
            }
            default:return;
        }

        Model model = new Model(classificator,parametrizator);

        for(Data data : trainData){
            model.train(data.text,data.classes);
        }

        double accuracy = testData(testData,model);

        //TODO ulozit model

        System.out.println("Model saved, accuracy: "+accuracy+"%");
    }

    public static double testData(ArrayList<Data> testData, Model model){
        double testingDataLength = testData.size();
        int numOfErrors = 0;

        for(Data data : testData){
            String resultClass = model.getClass(data.text);
            if(!data.classes.contains(resultClass)){
                numOfErrors++;
            }
        }
        return 1-(numOfErrors/testingDataLength);
    }

    public static void classify(String modelName){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JLabel modelNameLabel = new JLabel("Model: "+modelName);
        JLabel resultClassLabel = new JLabel("Class: ");
        northPanel.add(modelNameLabel);
        northPanel.add(Box.createHorizontalStrut(10));
        northPanel.add(resultClassLabel);
        frame.add(northPanel,BorderLayout.NORTH);

        JButton classifyButton = new JButton("Classify");
        frame.add(classifyButton,BorderLayout.SOUTH);

        JTextArea textArea = new JTextArea();
        frame.add(textArea,BorderLayout.CENTER);

        Model model = null;
        //TODO nacist model ze souboru

        classifyButton.addActionListener(e -> {
            String resultClass = getTextClass(model,textArea.getText() );
            resultClassLabel.setText("Class: "+resultClass);
        });

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private static String getTextClass(Model model, String text) {
        //TODO return model.getClass(text);
        return "";
    }

    private static ArrayList<Data> getDataFromFiles(File[] files) throws IOException {
        ArrayList<Data> data = new ArrayList<>();

        for(File file : files ){
            ArrayList<String> classes = new ArrayList<String>();
            StringBuilder text = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                line = br.readLine();
                classes.addAll(Arrays.asList(line.split(" ")));
                br.readLine();
                while ((line = br.readLine()) != null) {
                    text.append(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            data.add(new Data(classes,text.toString()));
        }

        return data;
    }


}
