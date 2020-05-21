package uir;

import uir.Classificators.BayesClassificator;
import uir.Classificators.KmeansClassificator;
import uir.Containers.Data;
import uir.Containers.ParamData;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
       /* ArrayList<String> classes1 = new ArrayList<String>();
        classes1.add("ano");
        classes1.add("ne");
        ArrayList<ParamData> pd = new ArrayList<ParamData>();

        ArrayList<String> pd1c = new ArrayList<String>();
        pd1c.add("ano");
        ParamData pd1 = new ParamData(pd1c,new byte[]{1,1,0});
        pd.add(pd1);

        ArrayList<String> pd2c = new ArrayList<String>();
        pd2c.add("ano");
        ParamData pd2 = new ParamData(pd2c,new byte[]{1,1,1});
        pd.add(pd2);

        ArrayList<String> pd3c = new ArrayList<String>();
        pd3c.add("ano");
        ParamData pd3 = new ParamData(pd3c,new byte[]{0,1,1});
        pd.add(pd3);

        ArrayList<String> pd4c = new ArrayList<String>();
        pd4c.add("ne");
        ParamData pd4 = new ParamData(pd4c,new byte[]{0,0,0});
        pd.add(pd4);

        ArrayList<String> pd5c = new ArrayList<String>();
        pd5c.add("ne");
        ParamData pd5 = new ParamData(pd5c,new byte[]{0,1,0});
        pd.add(pd5);

        KmeansClassificator bc = new KmeansClassificator(classes1,pd);
        System.out.print(bc.getClass(new byte[]{0,0,1}));*/

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
        Model model = new Model(classificatorType,parametrizatorType,trainData, classes);

        double accuracy = testData(testData,model);

        //TODO ulozit model

        System.out.println("Model saved, accuracy: "+accuracy+"%");
    }

    public static double testData(ArrayList<Data> testData, Model model){
        double testingDataLength = testData.size();
        int numOfErrors = 0;

        for(Data data : testData){
            String resultClass = model.getClass(data.getText());
            System.out.println(resultClass+" "+ Arrays.toString(data.getClasses().toArray()));
            if(!data.getClasses().contains(resultClass)){
                numOfErrors++;
            }
        }
        double acc = 100*(1-(numOfErrors/testingDataLength));
        return Math.round(acc * 10000.0) / 10000.0;
    }

    public static void classify(String modelName){
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(800,600));
        frame.setLayout(new BorderLayout());

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JLabel modelNameLabel = new JLabel("uir.Model: "+modelName);
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
