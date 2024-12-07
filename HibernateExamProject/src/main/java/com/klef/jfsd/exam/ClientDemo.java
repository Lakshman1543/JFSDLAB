package com.klef.jfsd.exam;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;


public class ClientDemo {
	public static void main(String[] args) {
		ClientDemo cd = new ClientDemo();
		//cd.insertcustomer();
		cd.restrictiondemo();
	}
	
	public void insertcustomer() {
		Configuration configuration = new Configuration();
		configuration.configure("hibernate.cfg.xml");
		
		SessionFactory sf = configuration.buildSessionFactory();
		Session session = sf.openSession();
		
		Transaction t = session.beginTransaction();
		
		Customer c = new Customer();
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter Customer ID:");
		int cid = sc.nextInt();
		System.out.println("Enter Customer Name:");
		String cname = sc.next();
		sc.nextLine();
		System.out.println("Enter Student Email:");
		String cemail = sc.next();
		System.out.println("Enter Student Age:");
		int cage = sc.nextInt();
		System.out.println("Enter Student Location:");
		String clocation = sc.next();
		
		c.setID(cid);
		c.setName(cname);
		c.setEmail(cemail);
		c.setAge(cage);
		c.setLocation(clocation);
		
		session.merge(c);
		t.commit();
		
		System.out.println("Customer Added Successfully");
		session.close();
		sf.close();
	}
	
	public void restrictiondemo() {
	    Configuration configuration = new Configuration();
	    configuration.configure("hibernate.cfg.xml");
	    
	    // Create SessionFactory and open session
	    SessionFactory sf = configuration.buildSessionFactory();
	    Session session = sf.openSession();
	    
	    // CriteriaBuilder and CriteriaQuery
	    CriteriaBuilder cb = session.getCriteriaBuilder();
	    CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);
	    Root<Customer> root = cq.from(Customer.class);
	    
	    Predicate lessThan = cb.lessThan(root.get("Age"), 50);
	    Predicate greaterThan = cb.greaterThan(root.get("Age"), 15);
	    Predicate le = cb.le(root.get("Age"), 40);
	    Predicate ge = cb.ge(root.get("Age"), 40);
	    Predicate ne = cb.notEqual(root.get("Location"), "VJA");
	    Predicate like = cb.like(root.get("Location"), "C%");

	    cq.select(root).where(cb.or(lessThan, greaterThan, le, ge, ne, like));
	    
	    List<Customer> customers = session.createQuery(cq).getResultList();
	    
	    session.getTransaction().begin();
	    session.createQuery(cq).list();
	    
	    for(Customer customer : customers) {
	        System.out.println(customer);
	    }
	    
	    session.close();
	    sf.close();
	}

}