package com.hostelbooking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hostelbooking.entity.Program;

@Repository
public interface ProgramRepository extends JpaRepository<Program, Long> {

	@Query(value = "SELECT * FROM programs WHERE starred = :starred", nativeQuery = true)
	List<Program> findProgramsByStarred(@Param("starred") boolean starred);

	@Query("SELECT p FROM Program p ORDER BY p.isStarred DESC, p.name ASC")
	List<Program> findAllOrderedByStarred();

	@Query(value = "SELECT * FROM program WHERE name = :name", nativeQuery = true)
	Optional<Program> findByNameNative(@Param("name") String name);

	@Query("SELECT p.name FROM Program p WHERE p.name LIKE %:query%")
	List<String> findProgramNamesByNameContaining(@Param("query") String query);

}
