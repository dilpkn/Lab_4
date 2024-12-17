import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Student {
    private final int number;
    private final String name;
    private final String secondName;
    private final String surname;
    private final int course;
    private final int group;
    private final HashMap<Integer, Vector<Exam>> map = new HashMap<>();

    public Student(int number, String surname, String name, String secondname, int course, int group) {
        this.number = number;
        this.name = name;
        this.secondName = secondname;
        this.surname = surname;
        this.course = course;
        this.group = group;
    }

    public Student() {
        this.number = 0;
        this.name = "";
        this.secondName = "";
        this.surname = "";
        this.course = 0;
        this.group = 0;
    }

    public Student(HashMap map) {
        number = 0;
        name = "";
        secondName = "";
        surname = "";
        course = 0;
        group = 0;
    }

    public void setData(int EX, int mark, String name_sub, int StudakTEMP) {
        Exam.Subj TEMP = new Exam.Subj(mark, name_sub);
        if (map.containsKey(EX)){
            Vector<Exam> TEMP1 = map.get(EX);
            if (!TEMP1.isEmpty()) {
                TEMP1.get(0).getExam().add(TEMP); // Добавляем в первый экзамен (или используем другой критерий)
            }
        } else {
            Vector<Exam.Subj> TEMP1 = new Vector<>();
            TEMP1.add(TEMP);
            Exam ex1 = new Exam(TEMP1);
            Vector<Exam> exams = new Vector<>();
            exams.add(ex1);
            map.put(EX, exams);
        }
    }


    @Override
    public String toString() {
        String ST = new String();
        ST = number + " " + surname + " " + name + " " + secondName + " " + course + " " + group;
        if(!map.isEmpty()) {
            for (Vector<Exam> exas : map.values()) {
                for (Exam ex : exas) {
                    String ST1 = new String();
                    ST1 = map.keySet().toString();
                    ST = ST + "\n" + ST1;
                    for (Exam.Subj subje : ex.getExam()) {
                        ST1 = new String();
                        ST1 = subje.getNamSub() + " - " + subje.getMark();
                        ST = ST + "\n"+ ST1;
                    }
                }
            }
        }
        return ST;
    }

    public int getNumber() {
        return number;
    }

    class Exam {
        private final Vector<Subj> ex;

        public Exam(Vector<Subj> ex) {
            this.ex = ex;
        }

        public Vector<Subj> getExam() {
            return new Vector<>(ex);
        }

        static class Subj {
            private int mark;
            private String name_sub;

            public Subj() {
            }

            public Subj(int mark, String nam_sub) {
                this.mark = mark;
                this.name_sub = nam_sub;
            }

            public int getMark() {
                return mark;
            }

            public void setMark(int mark) {
                this.mark = mark;
            }

            public String getNamSub() {
                return name_sub;
            }

            public void setNamSub(String name_sub) {
                this.name_sub = name_sub;
            }

            @Override
            public String toString() {
                return "Предмет: " + name_sub+
                        "Оценка: " + mark;
            }
        }
    }
}

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.print("Write type of input file: ");
        Scanner in = new Scanner(System.in);
        String s = in.next();
        ArrayList<Student> people = new ArrayList<>();
        if (s.equalsIgnoreCase("txt")) {
            File stud = new File("students.txt");
            Scanner scan = new Scanner(stud);
            int i = 0;
            while (scan.hasNextLine()) {
                String line = scan.nextLine();
                String[] st = line.split(" ");
                people.add(new Student(Integer.parseInt(st[0]), st[1], st[2], st[3], Integer.parseInt(st[4]), Integer.parseInt(st[5])));
            }
        } else if (s.equalsIgnoreCase("json")) {
            File studj = new File("students.json");
            people = new ObjectMapper().readValue(studj, new TypeReference<ArrayList<Student>>() {
            });
        } else {
            throw new Exception("ERROR!!!WRONG TYPE OF FILE!!!");
        }

        for (Student temp : people)
            System.out.println(temp);

        String write = """
                \nSelect the exam number from the list or press E to complete:
                1) Math
                2) Programming
                3) English
                """;
        System.out.println(write);
        String n;
        int count = 0;
        n = in.next();
        File exam = null;
        upperWhile:
        while (n.equalsIgnoreCase("E") || count <2) {
            if (n.equalsIgnoreCase(String.valueOf(1)))
                exam = new File("MA.txt");
            else if (n.equalsIgnoreCase(String.valueOf(2)))
                exam = new File("progr.txt");
            else if (n.equalsIgnoreCase(String.valueOf(3)))
                exam = new File("eng.txt");
            Scanner ex_sc = new Scanner(exam);
            String nameTemp = ex_sc.nextLine();
            int numberOfSesseonTemp = ex_sc.nextInt();
            while (ex_sc.hasNextLine()) {
                int studakTemp = ex_sc.nextInt();
                int markTemp = ex_sc.nextInt();
                for (Student temp1 : people) {
                    if (temp1.getNumber() == studakTemp) {
                        temp1.setData(numberOfSesseonTemp,markTemp,nameTemp,studakTemp);
                    }
                }
            }
            count++;
            n = in.next();
            if(n.equalsIgnoreCase("E")||count>=2){
                break upperWhile;
            }
        }
        for(Student te: people){
            System.out.println(te);
        }
        // write in JSON
        File fjson = new File("students.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValue(fjson, people);
    }
}
