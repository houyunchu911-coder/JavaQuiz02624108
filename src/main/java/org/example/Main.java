import java.util.ArrayList;

    abstract class Assessment {

        private String title;
        private double maxMark;

        public Assessment(String title) {
            this.title = title;
            this.maxMark = 100;
        }

        public String getTitle() {
            return title;
        }

        public double getMaxMark() {
            return maxMark;
        }

        public abstract double getMark();
    }

    class ProgrammingTest extends Assessment {

        private double mark;
        private boolean explained;

        public ProgrammingTest(double mark, boolean explained) {
            super(AssessmentFeed.PROGRAMMING_TEST);
            this.mark = mark;
            this.explained = explained;
        }

        @Override
        public double getMark() {
            if (explained) {
                return mark;
            }
            return Math.min(mark, 40);
        }
    }

    class AIProject extends Assessment {

        private double functionality;
        private double verification;

        public AIProject(double functionality, double verification) {
            super(AssessmentFeed.AI_PROJECT);
            this.functionality = functionality;
            this.verification = verification;
        }

        @Override
        public double getMark() {
            return functionality * 0.4 + verification * 0.6;
        }
    }

    class DesignViva extends Assessment {

        private double design;
        private double explanation;

        public DesignViva(double design, double explanation) {
            super(AssessmentFeed.DESIGN_VIVA);
            this.design = design;
            this.explanation = explanation;
        }

        @Override
        public double getMark() {
            return design * 0.5 + explanation * 0.5;
        }
    }

    class Student {

        private String name;
        private String cid;
        private String type;

        private ArrayList<Assessment> assessments =
                new ArrayList<>();

        public Student(String name, String cid, String type) {
            this.name = name;
            this.cid = cid;
            this.type = type;
        }

        public void addAssessment(Assessment a) {
            assessments.add(a);
        }

        public double getOverallMark() {

            double total = 0;

            for (Assessment a : assessments) {

                if (type.equals("UG")) {

                    if (a instanceof ProgrammingTest) {
                        total += a.getMark() * 0.3;
                    }

                    if (a instanceof AIProject) {
                        total += a.getMark() * 0.7;
                    }

                } else if (type.equals("MSc")) {

                    if (a instanceof ProgrammingTest) {
                        total += a.getMark() * 0.25;
                    }

                    if (a instanceof AIProject) {
                        total += a.getMark() * 0.45;
                    }

                    if (a instanceof DesignViva) {
                        total += a.getMark() * 0.3;
                    }
                }
            }

            return total;
        }

        public void printReport() {

            System.out.println("Name: " + name);
            System.out.println("CID: " + cid);
            System.out.println("Type: " + type);

            for (Assessment a : assessments) {
                System.out.printf("%s: %.2f%%%n",
                        a.getTitle(), a.getMark());
            }

            System.out.printf("Overall: %.2f%%%n",
                    getOverallMark());

            System.out.println();
        }
    }

    public class Main {

        public static void main(String[] args) {

            ArrayList<Student> students = new ArrayList<>();

            for (String cid : AssessmentFeed.getCIDs()) {

                String name = AssessmentFeed.getStudentName(cid);
                String type = AssessmentFeed.getStudentType(cid);

                Student student = new Student(name, cid, type);

                double mark = AssessmentFeed.getResult(
                        cid,
                        AssessmentFeed.PROGRAMMING_TEST,
                        AssessmentFeed.PROGRAM_MARK);

                boolean explained = AssessmentFeed.getResult(
                        cid,
                        AssessmentFeed.PROGRAMMING_TEST,
                        AssessmentFeed.EXPLAINED_SUCCESSFULLY) == 1;

                student.addAssessment(
                        new ProgrammingTest(mark, explained));

                double functionality = AssessmentFeed.getResult(
                        cid,
                        AssessmentFeed.AI_PROJECT,
                        AssessmentFeed.FUNCTIONALITY_MARK);

                double verification = AssessmentFeed.getResult(
                        cid,
                        AssessmentFeed.AI_PROJECT,
                        AssessmentFeed.VERIFICATION_MARK);

                student.addAssessment(
                        new AIProject(functionality, verification));

                if (type.equals("MSc")) {

                    double design = AssessmentFeed.getResult(
                            cid,
                            AssessmentFeed.DESIGN_VIVA,
                            AssessmentFeed.DESIGN_MARK);

                    double explanation = AssessmentFeed.getResult(
                            cid,
                            AssessmentFeed.DESIGN_VIVA,
                            AssessmentFeed.EXPLANATION_MARK);

                    student.addAssessment(
                            new DesignViva(design, explanation));
                }

                students.add(student);
            }

            for (Student s : students) {
                s.printReport();
            }
        }
    }
