import com.softserve.task.ormshared.annotations.*;

import java.sql.Date;
import java.util.Set;

@Table(name = "company")
@Entity(name = "company_entity")
public class Company {

    @EntityId
    @GeneratedValue
    private long id;

    @Column(defaultValue = "SomeName")
    private String name;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column
    @OneToMany(mappedBy = "company")
    private Set<Employee> employees;

    @OneToMany(mappedBy = "company")
    private	Set<Department> departments;

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Set<Department> getDepartments() {
		return departments;
	}

	@Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                //", createdAt=" + createdAt +
                ", employees=" + employees +
                '}';
    }

	public Company(long id, String name, Date createdAt) {
		super();
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
	}
	
	public Company() {
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Company other = (Company) obj;
		return this.id == other.id;
	}
}
