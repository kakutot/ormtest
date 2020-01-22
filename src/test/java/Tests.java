import com.softserve.task.ormcore.OrmFacade;
import com.softserve.task.ormcore.session.ISession;
import com.softserve.task.ormcore.session.SessionFactory;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

//import java.util.Date;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Tests {
    private final static String DB_NAME = "ormtest2";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "21272829ab";
    private static ISession session;
    private static Date defaultDate;
    private final static StringBuilder connectionUrl = new StringBuilder("jdbc:mysql://localhost:3306/ormtest2?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC");

    @BeforeClass
    public static void createTestDb()
            throws SecurityException, SQLException, InterruptedException {

        try (Connection conn = DriverManager.getConnection(connectionUrl.toString(), USERNAME, PASSWORD);
             Statement insertStatement = conn.createStatement()) {
            String query = "DROP DATABASE " + DB_NAME;
            insertStatement.execute(query);
        }

        try (Connection conn = DriverManager.getConnection(connectionUrl.toString(), USERNAME, PASSWORD);
             Statement insertStatement = conn.createStatement()) {
            //creationStatement.execute("CREATE DATABASE " + DB_NAME);
            OrmFacade ormFacade = new OrmFacade();
            ormFacade.initOrm();
            session = SessionFactory.getInstance().createSession();
            SimpleDateFormat defaultFormat = new SimpleDateFormat("dd-MM-yyyy");
            try {
                defaultDate = new Date(defaultFormat.parse("11-11-2008").getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Company without relationships
            // First company with it's departments and employees
            // Employees : 1 2 4
            // Departments : 1 3
            String query = "INSERT INTO Company (id, name, created_at) VALUES (\"1\", \"FirstCompany\", \"2008-11-11\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Employee (id, name, company_id) VALUES (\"1\", \"Name1\", \"1\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Employee (id, name, company_id) VALUES (\"2\", \"Name2\", \"1\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Employee (id, name, company_id) VALUES (\"4\", \"Name4\", \"1\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Department (id, city, address, company_id) VALUES (\"1\", \"City1\", \"Address1\", \"1\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Department (id, city, address, company_id) VALUES (\"3\", \"City3\", \"Address3\", \"1\")";
            insertStatement.addBatch(query);
            // Second company with it's departments and employees
            // Employees : 3
            // Departments : 2
            query = "INSERT INTO Company (id, name, created_at) VALUES (\"2\", \"SecondCompany\", \"2008-11-11\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Employee (id, name, company_id) VALUES (\"3\", \"Name3\", \"2\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Department (id, city, address, company_id) VALUES (\"2\", \"City2\", \"Address2\", \"2\")";
            insertStatement.addBatch(query);
            // Second company with it's departments and employees
            // Employees : none
            // Departments : none
            query = "INSERT INTO Company (id, name, created_at) VALUES (\"3\", \"ThirdCompany\", \"2008-11-11\")";
            insertStatement.addBatch(query);
            query = "INSERT INTO Company (id, name, created_at) VALUES (\"4\", \"FourthCompany\", \"2008-11-11\")";
            insertStatement.addBatch(query);
            insertStatement.executeBatch();
        }
    }

    @Test
    public void AshouldFind_All_Outer()
            throws SQLException, IllegalArgumentException {
        List<Employee> employeeList = session.selectAll(Employee.class).build();

        assertEquals(4, employeeList.size());
        assertEquals(new Employee(1, "Name1", null), employeeList.get(0));
        assertEquals(new Employee(2, "Name2", null), employeeList.get(1));
        assertEquals(new Employee(3, "Name3", null), employeeList.get(2));
        assertEquals(new Employee(4, "Name4", null), employeeList.get(3));
    }

    @Test
    public void BshouldFind_All_Inner()
            throws SQLException, IllegalArgumentException {
        List<Company> companyList = session.selectAll(Company.class).build();

        assertEquals(4, companyList.size());

        assertEquals(new Company(1, "FirstCompany", defaultDate), companyList.get(0));
        assertEquals(new Company(2, "SecondCompany", defaultDate), companyList.get(1));
        assertEquals(new Company(3, "ThirdCompany", defaultDate), companyList.get(2));
        assertEquals(new Company(4, "FourthCompany", defaultDate), companyList.get(3));
    }

    @Test
    public void CshouldFind_Where_Outer()
            throws SQLException, IllegalArgumentException {
        Company expected = new Company(2, "SecondCompany", defaultDate);

        List<Company> companyList = session.selectAll(Company.class).where("id", 2).build();
        Company actual = companyList.get(0);

        assertEquals(1, companyList.size());
        assertEquals(expected, actual);
    }

	@Test
	public void DshouldFind_Where_Inner()
			throws SQLException, IllegalArgumentException{
		Department expected = new Department(2, "City2", "Address2", null);

		List<Department> departmentList = session.selectAll(Department.class).where("address", "Address2").build();
		Department actual = departmentList.get(0);

		assertEquals(1, departmentList.size());
		assertEquals(expected, actual);
	}

	@Test
	public void EshouldFind_IncludeInnerEntity()
			throws SQLException, IllegalArgumentException, SecurityException {
		List<Company> companyList = session.selectAll(Company.class).include(Employee.class).where("id", 1).build();

		Set<Employee> employeeSet = new HashSet<>(companyList.get(0).getEmployees());

		assertEquals(3, employeeSet.size());
		assertTrue(employeeSet.contains(new Employee(1, "Name1", null)));
		assertTrue(employeeSet.contains(new Employee(2, "Name2", null)));
		assertTrue(employeeSet.contains(new Employee(4, "Name4", null)));
	}

	@Test
	public void FshouldFind_IncludeInnerEntity_ifNoEntity()
			throws SQLException, IllegalArgumentException {
		List<Company> companyList = session.selectAll(Company.class).include(Employee.class).where("id", 3).build();

		Set<Employee> employeeSet = new HashSet<>(companyList.get(0).getEmployees());

		assertEquals(0, employeeSet.size());
	}

	@Test
	public void GshouldFind_IncludeTwoInnerEntities()
			throws SQLException, IllegalArgumentException {
		List<Company> companyList = session.selectAll(Company.class).include(Employee.class, Department.class)
				.where("id", 1).build();

		Set<Employee> employeeSet = new HashSet<>(companyList.get(0).getEmployees());
		Set<Department> departmentSet = new HashSet<>(companyList.get(0).getDepartments());

		assertEquals(3, employeeSet.size());
		assertTrue(employeeSet.contains(new Employee(1, "Name1", null)));
		assertTrue(employeeSet.contains(new Employee(2, "Name2", null)));
		assertTrue(employeeSet.contains(new Employee(4, "Name4", null)));

		assertEquals(2, departmentSet.size());
		assertTrue(departmentSet.contains(new Department(1, "City1", "Address1", null)));
		assertTrue(departmentSet.contains(new Department(3, "City3", "Address3", null)));
	}

	@Test
    public void HshouldFind_IncludeOuterEntity()
		throws SQLException, IllegalArgumentException, SecurityException {
		Company expected = new Company(2, "SecondCompany", defaultDate);
		List<Employee> employeeList = session.selectAll(Employee.class).include(Company.class).where("id", 3).build();
		Company actual = employeeList.get(0).getCompany();

		assertEquals(expected, actual);

	}

    @Test
    public void IshouldDeleteEntity() throws SQLException {
        List<Company> companiesBefore = session.selectAll(Company.class).build();
        Company company = session.selectById(Company.class, 4).build();
        assertEquals(4, company.getId());
        assertTrue(companiesBefore.contains(company));

        session.delete(company);

        List<Company> companiesAfter = session.selectAll(Company.class).build();
        assertFalse(companiesAfter.contains(company));
    }

	@Test
    public void GshouldPersistEntity() throws SQLException {
        Company company = new Company();
        company.setName("SomeTestName");
        company.setCreatedAt(defaultDate);
        List<Company> companiesBefore = session.selectAll(Company.class).build();
        assertFalse(companiesBefore.contains(company));

        session.persist(company);

        List<Company> companiesAfter = session.selectAll(Company.class).build();
        assertTrue(companiesAfter.contains(company));
    }

    @Test
    public void KshouldPersistEntityWithParent() throws SQLException {
        Employee employee = new Employee();
        Company company = session.selectAll(Company.class).build().get(0);
        employee.setName("EmployeeTestName");
        employee.setCompany(company);

        List<Employee> employeesBefore = session.selectAll(Employee.class).build();
        assertFalse(employeesBefore.contains(employee));

        session.persist(employee);

        List<Employee> employeesAfter = session.selectAll(Employee.class).build();
        assertTrue(employeesAfter.contains(employee));
    }

    @Test
    public void LshouldSaveEntity() throws SQLException {
        Company company = new Company();
        company.setName("SomeTestName");
        company.setCreatedAt(defaultDate);
        List<Company> companiesBefore = session.selectAll(Company.class).build();
        assertFalse(companiesBefore.contains(company));

        long id = session.save(company);

        List<Company> companiesAfter = session.selectAll(Company.class).build();
        assertTrue(companiesAfter.contains(company));

        assertTrue(id > 0);
    }

    @Test
    public void MshouldSaveEntityWithParent() throws SQLException {
        Employee employee = new Employee();
        Company company = session.selectAll(Company.class).build().get(0);
        employee.setName("EmployeeTestName");
        employee.setCompany(company);

        List<Employee> employeesBefore = session.selectAll(Employee.class).build();
        assertFalse(employeesBefore.contains(employee));

        Integer id = session.save(employee);

        List<Employee> employeesAfter = session.selectAll(Employee.class).build();
        assertTrue(employeesAfter.contains(employee));

        assertTrue(id > 0);
    }

    @Test
    public void NshouldUpdateEntity() throws SQLException {
        Company company = session.selectAll(Company.class).build().get(0);
        long companyId = company.getId();
        String newName = "NewCompanyName";

        company.setName(newName);
        session.update(company);

        Company companyAfterUpdate = session.selectById(Company.class, companyId).build();
        String companyName = companyAfterUpdate.getName();
        assertEquals(companyName, newName);
    }

    @Test
    public void OshouldUpdateEntityWithParent() throws SQLException {
        Employee employee = session.selectAll(Employee.class).include(Company.class).build().get(0);
        long employeeId = employee.getId();
        String newName = "NewEmployeeName";

        employee.setName(newName);
        session.update(employee);

        Employee employeeAfterUpdate = session.selectById(Employee.class, employeeId).build();
        String employeeAfterUpdateName = employeeAfterUpdate.getName();

        assertEquals(employeeAfterUpdateName, newName);
    }
}
