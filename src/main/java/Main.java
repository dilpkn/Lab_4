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
    private final HashMap<Integer, Vector<Exam.Subj>> map = new HashMap<>();

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

    public void setData(int sessionNumber, int mark, String subjectName, int studentNumber) {
        Exam.Subj newSubject = new Exam.Subj(mark, subjectName);
        if (map.containsKey(sessionNumber)) {
            Vector<Exam.Subj> subjects = map.get(sessionNumber);
            boolean subjectExists = subjects.stream().anyMatch(subj -> subj.getNamSub().equals(subjectName));
            if (!subjectExists) {
                subjects.add(newSubject);
                map.put(sessionNumber,subjects);
            }
        } else {
            Vector<Exam.Subj> subjects = new Vector<>();
            subjects.add(newSubject);
            map.put(sessionNumber, subjects);
        }
    }

public void GrateStud() {
    StringBuilder ST = new StringBuilder(number + " " + surname + " " + name + " " + secondName);
    int sum = 0;
    if (!map.isEmpty()) {
        for (int sessionNumber : map.keySet()) {
            Vector<Exam.Subj> subjects = map.get(sessionNumber);
            for (Exam.Subj subject : subjects) {
                sum += subject.getMark();
            }
        }
    }
    ST.append("Общий балл = ").append(sum/3);
    if(sum/3>=8)
        System.out.println(ST + "\n");
}

    @Override
    public String toString() {
        StringBuilder ST = new StringBuilder(number + " " + surname + " " + name + " " + secondName + " " + course + " " + group);
        if (!map.isEmpty()) {
            for (int sessionNumber : map.keySet()) {
                ST.append("\nSession ").append(sessionNumber).append(":");
                Vector<Exam.Subj> subjects = map.get(sessionNumber);
                for (Exam.Subj subject : subjects) {
                    ST.append("\n  ").append(subject.getNamSub()).append(" - ").append(subject.getMark());
                }
            }
        }
        return ST.toString();
    }





    public int getNumber() {
        return number;
    }

    class Exam {
        private final Vector<Vector<Subj>> ex;

        public Exam(Vector<Vector<Subj>> ex) {
            this.ex = ex;
        }

        public Vector<Vector<Subj>> getExam() {
            return new Vector<Vector<Subj>>(ex);
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
        while (n.equalsIgnoreCase("E") || count <3) {
            count++;
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
            if(n.equalsIgnoreCase("E")||count==3){
                break upperWhile;
            }
            n = in.next();
        }
        for(Student te: people){
            System.out.println(te);
        }

        write = """
                \nDo you want to see a list of  great students:
                1) Yes
                2) No
                """;
        System.out.println(write);
        n = in.next();
        if(n.equalsIgnoreCase("Yes")){
            System.out.println("Список отличников вс баллом от 8:");
            for(Student temp1:people)
                temp1.GrateStud();
        }else{
            System.out.println("Список отличников выведен не будет!!");
        }

        // write in JSON
        File fjson = new File("students.json");
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT).writeValue(fjson, people);
    }
}
