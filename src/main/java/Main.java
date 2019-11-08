import org.hibernate.Criteria;
import org.hibernate.PropertyAccessException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/** Подключите в вашем проекте Hibernate и напишите код, выводящий информацию о каком-нибудь курсе. **/

public class Main
{
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/skillbox";
        String user = "root";
        String password = "testtest";
        int count = 0;
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from courses");
            while (resultSet.next()) {
                count = resultSet.getInt("id");
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
        Metadata metadata = new MetadataSources(registry).getMetadataBuilder().build();
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
        Session session = sessionFactory.openSession();
        
        for (int i = 1; i <= count; i++ ) {
            int courseId = i;
            Course course = session.get(Course.class, courseId);

            int teacherId = course.getId();
            Teacher teacher = session.get(Teacher.class, teacherId);
            System.out.println(courseId + ". " + course.getName() + " - " + teacher.getName());
        }

//        Criteria criteria = session.createCriteria(Course.class);
//        List<Course> courses = criteria.list();
//        courses.forEach(System.out::println);
//
//        Criteria criteria1 = session.createCriteria(Teacher.class);
//        List<Teacher> teachers = criteria1.list();
//        teachers.forEach(System.out::println);

        sessionFactory.close();
    }
}
