package com.bank.crm.dao;

import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.bank.crm.entity.AgentCustomerAssignment;
import com.bank.crm.entity.Customer;
import com.bank.crm.entity.User;

@Repository("BankDaoImpl")
@Scope("singleton")
@Transactional
public class BankDaoImpl extends HibernateDaoSupport implements BankDao {
	
	@Autowired
	@Qualifier("sessionFactory")
	public void setBankSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	public String acceptCustomer(int cid) {
		List<Customer> customerList = (List<Customer>) super.getHibernateTemplate().find("from Customer where cid=?", cid);
		Customer customer = customerList.get(0);
		customer.setStatus("Accepted");
		super.getHibernateTemplate().update(customer);
		return "accepted";
	}

	public String rejectCustomer(int cid) {
		List<Customer> customerList = (List<Customer>) super.getHibernateTemplate().find("from Customer where cid=?", cid);
		Customer customer = customerList.get(0);
		customer.setStatus("Rejected");
		super.getHibernateTemplate().update(customer);
		return "rejected";
	}

	/**
	 * Retrieves all Customers whose statuses are Pending, and haven't been
	 * accepted or rejected by the Bank Manager.
	 */
	public List<Customer> showPendingCustomers() {
		List<Customer> customerList = 
				(List<Customer>) super.getHibernateTemplate().find("from Customer where status=?", "Pending");
		System.out.println(customerList.get(0));
		return customerList;
	}
	
	/**
	 * Retrieves all Customers whose statuses are Accepted, having had their
	 * requests approved by the Bank Manager.
	 * @return
	 */
	public List<Customer> showAcceptedCustomers() {
		List<Customer> customerList = 
				(List<Customer>) super.getHibernateTemplate().find("from Customer where status=?", "Accepted");
		return customerList;
	}

	/**
	 * Retrieves all Agents that are currently online
	 */
	public List<String> showOnlineAgents() {
		System.out.println("In DAO layer");
		String hql = "select a.username from Authority a, User u " +
						"where a.authority=? " +
						"and u.status=1 " +
						"and a.username=u.username";
		List<String> agentList =
			(List<String>) super.getHibernateTemplate().find(hql, "ROLE_AGENT");
		return agentList;
	}

	public List<AgentCustomerAssignment> showAssignments() {
		String hql = "from AgentCustomerAssignment";
		List<AgentCustomerAssignment> assignmentList = (List<AgentCustomerAssignment>) super.getHibernateTemplate().find(hql);
		return assignmentList;
	}

	public void assign(String agentUsername, int cid) {
		// Get the agent
		String hql = "from User where username=?";
		List<User> agentList = (List<User>) super.getHibernateTemplate().find(hql, agentUsername);
		User agent = agentList.get(0);
		
		// Get the customer
		hql = "from Customer where cid=?";
		List<Customer> customerList = (List<Customer>) super.getHibernateTemplate().find(hql, cid);
		
		// Create the assignment
		AgentCustomerAssignment aca = new AgentCustomerAssignment();
		aca.setAgentUsername(agent.getUsername());
		aca.setCustomer(customerList.get(0));
		aca.setStatus(1);
		
		// Persist the assignment
		super.getHibernateTemplate().save(aca);
		
		//Change Customer status to Assigned
		Customer customer = customerList.get(0);
		customer.setStatus("Assigned");
		super.getHibernateTemplate().update(customer);
	}
}
