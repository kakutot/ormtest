package com.softserve.task.generated;

import com.softserve.task.ormshared.holders.ColumnHolder;
import com.softserve.task.ormshared.holders.DefaultRelationship;
import com.softserve.task.ormshared.holders.EntityHolder;
import com.softserve.task.ormshared.holders.FkInfoHolder;
import com.softserve.task.ormshared.holders.IdGeneratorType;
import com.softserve.task.ormshared.holders.IdHolder;
import com.softserve.task.ormshared.holders.MetaHolder;
import com.softserve.task.ormshared.holders.RelationshipType;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class MetaHolderImpl implements MetaHolder {
  @Override
  public Map<String, EntityHolder> getMetaInfo() {
    Map<String, EntityHolder> result = new HashMap<String, EntityHolder>();
    ColumnHolder columnHolder0 = new ColumnHolder("java.lang.Integer", "id", "id", false, "");
    IdHolder id0 = new IdHolder(columnHolder0, IdGeneratorType.IDENTITY);
    List columns0 = new ArrayList<ColumnHolder>();
    ColumnHolder column0 = new ColumnHolder("java.lang.String", "name", "name", false, "");
    columns0.add(column0);
    EntityHolder employee = new EntityHolder("employee", "employee", id0, columns0);
    ColumnHolder columnHolder1 = new ColumnHolder("java.lang.Long", "id", "id", false, "");
    IdHolder id1 = new IdHolder(columnHolder1, IdGeneratorType.IDENTITY);
    List columns1 = new ArrayList<ColumnHolder>();
    ColumnHolder column1 = new ColumnHolder("java.lang.String", "name", "name", true, "SomeName");
    columns1.add(column1);
    ColumnHolder column2 = new ColumnHolder("java.sql.Date", "createdAt", "created_at", false, "");
    columns1.add(column2);
    EntityHolder company = new EntityHolder("company_entity", "company", id1, columns1);
    ColumnHolder columnHolder2 = new ColumnHolder("java.lang.Long", "id", "id", false, "");
    IdHolder id2 = new IdHolder(columnHolder2, IdGeneratorType.IDENTITY);
    List columns2 = new ArrayList<ColumnHolder>();
    ColumnHolder column3 = new ColumnHolder("java.lang.String", "city", "city", true, "");
    columns2.add(column3);
    ColumnHolder column4 = new ColumnHolder("java.lang.String", "address", "address", true, "");
    columns2.add(column4);
    EntityHolder department = new EntityHolder("dept_entity", "department", id2, columns2);
    ColumnHolder columnHolderFkTable1 = new ColumnHolder("Company", "company", "company_id", true, "");
    ColumnHolder columnHolderOwnerTable1 = new ColumnHolder("java.lang.Long", "id", "id", false, "");
    FkInfoHolder fkInfoHolder1 = new FkInfoHolder(columnHolderFkTable1, columnHolderOwnerTable1, "SOME_TEST_FK_CONSTR", "ON DELETE NO ACTION \r\n"
            + " ON UPDATE NO ACTION");
    ColumnHolder containerFieldInParent1 = new ColumnHolder("java.util.Set<Employee>", "employees", "", false, "");
    DefaultRelationship relationship1 = new DefaultRelationship(company, employee, fkInfoHolder1, RelationshipType.ONE_TO_MANY_BIDIRECT, containerFieldInParent1);
    employee.addRelationship(relationship1);
    company.addRelationship(relationship1);
    result.put("Employee", employee);
    ColumnHolder columnHolderFkTable5 = new ColumnHolder("Company", "company", "company_id", true, "");
    ColumnHolder columnHolderOwnerTable5 = new ColumnHolder("java.lang.Long", "id", "id", false, "");
    FkInfoHolder fkInfoHolder5 = new FkInfoHolder(columnHolderFkTable5, columnHolderOwnerTable5, "fk_constraint_company_id", "ON DELETE NO ACTION \r\n"
            + " ON UPDATE NO ACTION");
    ColumnHolder containerFieldInParent5 = new ColumnHolder("java.util.Set<Department>", "departments", "", false, "");
    DefaultRelationship relationship5 = new DefaultRelationship(company, department, fkInfoHolder5, RelationshipType.ONE_TO_MANY_BIDIRECT, containerFieldInParent5);
    company.addRelationship(relationship5);
    department.addRelationship(relationship5);
    result.put("Company", company);
    result.put("Department", department);
    return result;
  }
}
