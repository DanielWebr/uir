package uir;

import uir.Classificators.BayesClassificator;
import uir.Classificators.KmeansClassificator;
import uir.Containers.Data;
import uir.Containers.ParamData;

import javax.swing.*;
import javax.swing.event.AncestorListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Program started");
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
        System.out.println("Training model modelName -> classification: "+classificatorType.name()+", parametrization: "+parametrizatorType.name());
        Model model = new Model(classificatorType,parametrizatorType,trainData, classes);
        System.out.println("Testing model ");
        double accuracy = testData(testData,model);
        System.out.println("--- Model accuracy "+accuracy+" ---");
        System.out.println("Saving model");
        try {
            saveModel(model,modelName);
            System.out.println("Model saved to savedModels folder");
        } catch (IOException e) {
            System.out.println("Error saving model");
        }
        System.out.println("Program end...");
    }

    public static void saveModel(Model model, String name) throws IOException {
        FileOutputStream f = new FileOutputStream(new File("savedModels\\"+name+".obj"));
        ObjectOutputStream o = new ObjectOutputStream(f);
        o.writeObject(model);
        o.close();
        f.close();
    }

    public static Model loadModel(String name){
        System.out.println("Loading model "+name);
        Model model = null;
        try {
            FileInputStream fi = new FileInputStream(new File("savedModels\\"+name+".obj"));
            ObjectInputStream oi = new ObjectInputStream(fi);
            model = (Model) oi.readObject();
            oi.close();
            fi.close();
            System.out.println("Model loaded");
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error initializing stream");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static double testData(ArrayList<Data> testData, Model model){
        double testingDataLength = testData.size();
        int numOfErrors = 0;

        for(Data data : testData){
            String resultClass = model.getClass(data.getText());
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
        frame.setTitle("UIR text classification");

        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout());

        JLabel modelNameLabel = new JLabel("model: "+modelName);
        JLabel resultClassLabel = new JLabel("Class: ");
        northPanel.add(modelNameLabel);
        northPanel.add(Box.createHorizontalStrut(10));
        northPanel.add(resultClassLabel);
        frame.add(northPanel,BorderLayout.NORTH);

        JButton classifyButton = new JButton("Classify");
        classifyButton.setEnabled(false);
        frame.add(classifyButton,BorderLayout.SOUTH);

        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        frame.add(Box.createHorizontalStrut(10),BorderLayout.WEST);
        frame.add(Box.createHorizontalStrut(10),BorderLayout.EAST);
        frame.add(textArea,BorderLayout.CENTER);

        Model model = loadModel(modelName);

        classifyButton.addActionListener(e -> {
            String resultClass =  model.getClass(textArea.getText() );
            resultClassLabel.setText("Class: "+classToString(resultClass));
        });

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if(textArea.getText().equals(""))classifyButton.setEnabled(false);
                else classifyButton.setEnabled(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if(textArea.getText().equals(""))classifyButton.setEnabled(false);
                else classifyButton.setEnabled(true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
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

    public static String classToString(String classificationClass){
        switch(classificationClass){
            case "ces":{
                return "CESTOVÁNÍ";
            }
            case "dop":{
                return "DOPRAVA";
            }
            case "fin":{
                return "FINANČNICTVÍ A OBCHOD";
            }
            case "kri":{
                return "KRIMINALITA A PRÁVO";
            }
            case "kul":{
                return "KULTURA";
            }
            case "nab":{
                return "NÁBOŽENSTVÍ";
            }
            case "nes":{
                return "NEŠTĚSTÍ A KATASTROFY";
            }
            case "pol":{
                return "POLITIKA";
            }
            case "pri":{
                return "PŘÍRODA A POČASÍ";
            }
            case "pru":{
                return "PRŮMYSL";
            }
            case "reg":{
                return "REGION";
            }
            case "rek":{
                return "REKLAMA";
            }
            case "sko":{
                return "ŠKOLSTVÍ";
            }
            case "slu":{
                return "SLUŽBY";
            }
            case "soc":{
                return "SOCIÁLNÍ PROBLEMATIKA";
            }
            case "spo":{
                return "SPORT";
            }
            case "ved":{
                return "VĚDA A TECHNIKA";
            }
            case "zdr":{
                return "ZDRAVOTNICTVÍ";
            }
            case "voj":{
                return "VOJENSTVÍ";
            }
            case "zem":{
                return "ZEMĚDĚLSTVÍ";
            }
            case "ost":{
                return "OSTATNÍ";
            }
        }
        return "EROR";
    }

}
