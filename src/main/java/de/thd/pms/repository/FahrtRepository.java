package de.thd.pms.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import de.thd.pms.model.Boot;
import de.thd.pms.model.Fahrt;
import de.thd.pms.model.Person;

public interface FahrtRepository extends CrudRepository<Fahrt, Long> {

	Set<Fahrt> findByBoot(Boot boot);

	@Query("select f from Fahrt f where f.boot.id = ?1")
	Set<Fahrt> findByBootId(long bootId);

	List<Fahrt> findByAnkunftIsNull();

	@Query("select distinct f from Fahrt f JOIN f.ruderer r where r = ?1")
	List<Fahrt> findByRuderer(Person person);

}
