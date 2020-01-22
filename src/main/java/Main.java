import com.softserve.task.ormcore.OrmFacade;
import com.softserve.task.ormcore.session.ISession;
import com.softserve.task.ormcore.session.SessionFactory;
import com.softserve.task.ormcore.session.transaction.Transaction;
import com.softserve.task.ormcore.session.transaction.TransactionException;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String args[]) throws InterruptedException, SQLException {
        OrmFacade ormFacade = new OrmFacade();
        ormFacade.initOrm();
        
        ISession session = SessionFactory.getInstance().createSession();
        Transaction transaction = session.createTransaction();
        try {
            List<Company> companies = session.selectAll(Company.class).include(Employee.class, Department.class).build();
            System.out.println(new ArrayList<>(companies));
            Company companyS = session.selectById(Company.class, 1).include(Employee.class).build();
            System.out.println("Company is null" + (companyS == null));
            Company company = new Company();
            company.setCreatedAt(new Date(System.currentTimeMillis()));
            company.setName("Hello");

            session.save(company);

            Employee employee = new Employee();
            employee.setName("EmployeeTest");
            employee.setCompany(company);
            Integer employeeId = session.save(employee);
            System.out.println("Id " + employeeId);

            Employee employee2 = new Employee();
            employee2.setName("EmployeeTest2");
            employee2.setCompany(company);
            session.save(employee2);

            Employee employee3 = new Employee();
            employee3.setName("EmployeeTest3");
            employee3.setCompany(company);
            Integer employee3Id = session.save(employee3);


            List<Employee> employees = session.selectAll(Employee.class).build();
            System.out.println(new ArrayList<>(employees));


            Employee test = session.selectAll(Employee.class).include(Company.class).where("name", "EmployeeTest")
                                    .build().get(0);
            System.out.println(test);
            test.setName("New Name");
            session.update(test);

            Employee employee3S = session.selectById(Employee.class, employee3Id).build();
            session.delete(employee3S);

            List<Employee> employees2 = session.selectAll(Employee.class).build();
            System.out.println(new ArrayList<>(employees2));

            transaction.commitTransaction();
        } catch (TransactionException te) {
            if (transaction != null) {
                try {
                    transaction.rollback();
                    te.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                session.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}

