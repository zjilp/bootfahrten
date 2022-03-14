package de.thd.pms.dao;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import de.thd.pms.model.Boot;
import de.thd.pms.service.BootService;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration("/test-application-context.xml")
public class BootServiceTest extends AbstractDataAccessTest {
	private BootService bootService;
	private String[] tables = {"tbl_boot"};

	@Autowired
	public void setBootDao(BootService bootService) {
		this.bootService = bootService;
	}

	@Test
	public void testFindById() {
		// deleteFromTables is provided by the Superclass AbstractDataAccessTest
		// delete all rows from db table
		deleteFromTables(tables);
		// create new line in table
		bootService.create("Uno", 1, "Einer");
		bootService.create("Due", 2, "Zweier");
		Iterable<Boot> alleBoote = bootService.findAll();
		for (Boot p : alleBoote) {
			Long id = p.getId();
			Boot found = bootService.findById(id);
			assertEquals(p.getName(), found.getName());
		}
		// delete all rows from db table
		deleteFromTables(tables);
	}

	@Test
	public void testFindAll() {
		// already tested by testFindById()
		testFindById();
	}

	@Test
	public void testCreate() {
		String tabellennameBoot = "tbl_boot";
		String[] tables = {tabellennameBoot};
		deleteFromTables(tables);
		bootService.create("Deggendorf", 4, "Vierer");
		int rows = countRowsInTable(tabellennameBoot);
		assertEquals(1, rows, "Die DB muss eine Zeile enthalten");
		// delete all rows from db table
		deleteFromTables(tables);
	}

	@Test
	public void testFindFreie() {
		// delete all rows from db table
		deleteFromTables(tables);
		// create new line in table
		bootService.create("Uno", 1, "Einer");
		bootService.create("Due", 2, "Zweier");
		List<Boot> alleBoote = bootService.findFreie();
		for (Boot p : alleBoote) {
			Long id = p.getId();
			Boot found = bootService.findById(id);
			assertEquals(p.getName(), found.getName());
		}
		// delete all rows from db table
		deleteFromTables(tables);
	}

}
