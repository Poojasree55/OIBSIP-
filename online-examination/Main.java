//package OIBSIP-.Online-Examination;
import java.util.*;

class User {
    String username, password, name, email;
    User(String username, String password, String name, String email) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
    }
}

class Question {
    String question;
    String[] options;
    int correctOption;
    Question(String question, String[] options, int correctOption) {
        this.question = question;
        this.options = options;
        this.correctOption = correctOption;
    }
}

class AuthService {
    private User currentUser;
    private List<User> users;

    AuthService() {
        users = new ArrayList<>();
        users.add(new User("admin", "1234", "Admin", "admin@mail.com"));
    }

    public boolean login(String username, String password) {
        for(User u : users) {
            if(u.username.equals(username) && u.password.equals(password)) {
                currentUser = u;
                System.out.println("Login Successful!");
                return true;
            }
        }
        System.out.println("Invalid credentials!");
        return false;
    }

    public void updateProfile(String name, String email) {
        if(currentUser != null) {
            currentUser.name = name;
            currentUser.email = email;
            System.out.println("Profile updated!");
        }
    }

    public void changePassword(String newPassword) {
        if(currentUser != null) {
            currentUser.password = newPassword;
            System.out.println("Password changed!");
        }
    }

    public void logout() {
        currentUser = null;
        System.out.println("Logged out!");
    }

    public User getCurrentUser() {
        return currentUser;
    }
}

class QuizService {
    private List<Question> questions;
    private Map<Integer, Integer> answers = new HashMap<>();

    QuizService() {
        questions = new ArrayList<>();
        questions.add(new Question("What is 2+2?", new String[]{"1","2","4","5"}, 3));
        questions.add(new Question("Capital of France?", new String[]{"Berlin","Paris","London","Rome"}, 2));
    }

    public void startQuiz(int timeLimitSeconds) {
        Scanner sc = new Scanner(System.in);
        long endTime = System.currentTimeMillis() + timeLimitSeconds * 1000L;
        System.out.println("Quiz Started! You have " + timeLimitSeconds + " seconds.");

        for (int i = 0; i < questions.size(); i++) {
            if(System.currentTimeMillis() > endTime) {
                System.out.println("Time's up! Auto-submitting...");
                break;
            }
            Question q = questions.get(i);
            System.out.println((i+1)+". "+q.question);
            for(int j=0;j<q.options.length;j++)
                System.out.println((j+1)+". "+q.options[j]);
            System.out.print("Your answer: ");
            int ans = sc.nextInt();
            answers.put(i, ans);
        }
        calculateScore();
    }

    private void calculateScore() {
        int score = 0;
        for(int i=0;i<questions.size();i++) {
            if(answers.getOrDefault(i, -1) == questions.get(i).correctOption) score++;
        }
        System.out.println("Your score: "+score+"/"+questions.size());
    }
}

public class Main{
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();
        QuizService quiz = new QuizService();

        while(true) {
            System.out.print("Enter username: ");
            String u = sc.next();
            System.out.print("Enter password: ");
            String p = sc.next();

            if(auth.login(u,p)) break;
        }

        while(true) {
            System.out.println("\n1.Start Quiz\n2.Update Profile\n3.Change Password\n4.Logout");
            int choice = sc.nextInt();
            switch(choice) {
                case 1: quiz.startQuiz(30); break; //30 seconds timer
                case 2: 
                    System.out.print("Enter new name: ");
                    String name = sc.next();
                    System.out.print("Enter new email: ");
                    String email = sc.next();
                    auth.updateProfile(name, email);
                    break;
                case 3:
                    System.out.print("Enter new password: ");
                    String pass = sc.next();
                    auth.changePassword(pass);
                    break;
                case 4: 
                    auth.logout();
                    return;
            }
        }
    }
}




