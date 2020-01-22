import com.softserve.task.ormshared.annotations.*;

@Table(name = "employee")
@Entity(name = "employee")
public class Employee {

	@EntityId
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @ForeignKey(constraintName = "SOME_TEST_FK_CONSTR")
    private Company company;

    public Integer getId() {
        return id;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Company getCompany() {
        return company;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Employee(Integer id, String name, Company company) {
		this.id = id;
		this.name = name;
		this.company = company;
	}
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Employee other = (Employee) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public Employee() {
    	
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", company=" + company +
                '}';
    }
}
