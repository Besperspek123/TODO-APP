package com.example.Hibernate;

import com.example.ObjectsDataBase.CompletedTask;
import com.example.ObjectsDataBase.CurrentTask;
import com.example.ObjectsDataBase.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateSessionFactory {

    private static final SessionFactory sessionFactoryCurrentTaskClass = getCurrentFactoryCurrentTask();

    private static final SessionFactory sessionFactoryUserClass = getCurrentFactoryUser();

    private static final SessionFactory sessionFactoryCompletedTaskClass = getCurrentFactoryCompletedTask();

    private static SessionFactory getCurrentFactoryCompletedTask() {
        SessionFactory currentFactory;
        if (sessionFactoryCompletedTaskClass == null){
            Configuration configuration = new Configuration().configure().addAnnotatedClass(CompletedTask.class);
            currentFactory = configuration.buildSessionFactory();
        }
        else currentFactory = sessionFactoryCompletedTaskClass;
        return currentFactory;
    }

    private static SessionFactory getCurrentFactoryCurrentTask() {

        SessionFactory currentFactory;
        if (sessionFactoryCurrentTaskClass == null){
            Configuration configuration = new Configuration().configure().addAnnotatedClass(CurrentTask.class);
            currentFactory = configuration.buildSessionFactory();
        }
        else currentFactory = sessionFactoryCurrentTaskClass;
        return currentFactory;
    }

    public static Session getCurrentSessionCurrentTask() {
        return getCurrentFactoryCurrentTask().openSession();
    }

    public static Session getCurrentSessionCompletedTask() {
        return getCurrentFactoryCompletedTask().openSession();
    }



    private static SessionFactory getCurrentFactoryUser() {
        SessionFactory currentFactory;
        if (sessionFactoryUserClass == null){
            Configuration configuration = new Configuration().configure().addAnnotatedClass(User.class);
            currentFactory = configuration.buildSessionFactory();
        }
        else currentFactory = sessionFactoryUserClass;
        return currentFactory;
    }

    public static Session getCurrentSessionUser() {
        return getCurrentFactoryUser().openSession();
    }

    public static void shutdownFactors(){
        if(sessionFactoryUserClass != null && !sessionFactoryUserClass.isClosed()){
            try {
                getCurrentFactoryUser().close();
                System.out.println("fabric User session is closed");
            }
            catch (Exception e){
                System.out.println("Fabric can`t close");
            }


        }
        if(sessionFactoryCurrentTaskClass != null && !sessionFactoryCurrentTaskClass.isClosed()){
            try {
                getCurrentFactoryCurrentTask().close();
                System.out.println("fabric CurrentTask session is closed");
            }
            catch (Exception e){
                System.out.println("Fabric can`t close");

            }

        }
        if(sessionFactoryCompletedTaskClass != null && !sessionFactoryCompletedTaskClass.isClosed()){
            try {
                getCurrentFactoryCompletedTask().close();
                System.out.println("fabric CompletedTask session is closed");
            }
            catch (Exception e){
                System.out.println("Fabric can`t close");
            }

        }

    }

}
