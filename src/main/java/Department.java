import com.softserve.task.ormshared.annotations.*;

import java.io.Serializable;

@Table(name = "department")
@Entity(name = "dept_entity")
public class Department implements Serializable {
	
	private static final long serialVersionUID = 1L;

    @EntityId
    @GeneratedValue
	private long id;
	
	@Column
	private String city;
	
	@Column
	private String address;
	
	@ManyToOne
	@JoinColumn(name = "company_id")
	private Company company;
	
	public Department(long id, String city, String address, Company company) {
		this.id = id;
		this.city = city;
		this.address = address;
		this.company = company;
	}
	
	public Department() {
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	@Override
	public String toString() {
		return "Department [id=" + id + ", city=" + city + ", address=" + address + ", company=" + company + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		Department other = (Department) obj;
		if (id != other.id)
			return false;
		return true;
	}

}