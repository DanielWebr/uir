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

enum ClassificatorType {
    BAYES,
    CUSTOM1
}

enum ParametrizatorType {
    CUSTOM1,
    CUSTOM2,
    CUSTOM3
}

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
            File trainData = new File(args[1]);
            File testData = new File(args[2]);

            if(!classesFile.exists() ||  !trainData.exists()  || !testData.exists()){
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

            train(classes,trainData,testData,classificatorType,parametrizatorType,model);

        }

        if(classificationsMode){
            String model = args[0];
            classify(model);
        }
    }

    public static void train(ArrayList<String> classes, File trainDataFolder, File testDataFolder, ClassificatorType classificatorType, ParametrizatorType parametrizatorType, String modelName)  {
        Classificator classificator;
        Parametrizator parametrizator;

        File[] trainData = trainDataFolder.listFiles();
        File[] testData = testDataFolder.listFiles();

        if(trainData == null || testData == null){
            System.out.print("Wrong test or train data");
            return;
        }

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
        model.train(getDataFromFile(trainData));
        double accuracy = model.test(getDataFromFile(testData));
        //TODO ulozit model

        System.out.println("Model saved, accuracy: "+accuracy+"%");
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

        classifyButton.addActionListener(e -> {
            String resultClass = getTextClass(modelName,textArea.getText() );
            resultClassLabel.setText("Class: "+resultClass);
        });

        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
    }

    private static String getTextClass(String modelName, String text) {
        //TODO nacist model podle jmena
        //return model.getClass(text)
        return "Test";
    }

    private static ArrayList<Data> getDataFromFile(File[] files){
        ArrayList<Data> data = new ArrayList<>();

        for(File file : files ){
            ArrayList<String> classes = new ArrayList<String>();
            String text = "";

            //TODO vyndat z filu tridy a text

            data.add(new Data(classes,text));
        }

        return data;
    }


}
