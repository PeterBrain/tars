package at.fh.swenga.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import at.fh.swenga.model.Entry;

public interface EntryRepository extends JpaRepository<Entry, Integer> {

}
