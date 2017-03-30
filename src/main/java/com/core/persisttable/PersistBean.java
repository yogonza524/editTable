/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.core.persisttable;


import com.core.model.Person;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.primefaces.event.CellEditEvent;

/**
 * 
 * @author Gonzalo H. Mendoza
 * email: yogonza524@gmail.com
 * StackOverflow: http://stackoverflow.com/users/5079517/gonza
 */
@ManagedBean(name="persist")
@RequestScoped
public class PersistBean {
    
    private List<Person> persons;

    public List<Person> getPersons() {
        return persons;
    }

    public void setPersons(List<Person> persons) {
        this.persons = persons;
    }
    
    @PostConstruct
    public void init(){
        //init code
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        persons = session.createCriteria(Person.class).list();
        
        session.close();
    }
    
    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        
        FacesContext context = FacesContext.getCurrentInstance();
        Person person = context.getApplication().evaluateExpressionGet(context, "#{person}", Person.class);
        
        if (person != null) {
            //Get session
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction t = session.beginTransaction();
            
            session.update(person); 
            
            t.commit();
            
            session.close();
        }
         
        if(newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
            System.out.println(event.getNewValue());
        }
    }
    
}
