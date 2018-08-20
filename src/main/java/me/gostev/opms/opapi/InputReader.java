package me.gostev.opms.opapi;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class InputReader {

	private Map<String, Project> projects = new HashMap<>();
	private Map<String, Student> students = new HashMap<>();

	public InputReader(Reader in) throws IOException {

		CSVParser csvParser = new CSVParser(in, CSVFormat.DEFAULT);

		for (CSVRecord record : csvParser) {

			Project p = new Project(record.get(0));
			projects.put(p.getName(), p);

			int members = record.size();

			for (int i = 1; i < members; i++) {
				Student s = new Student(record.get(i), record.get(i));

				if (!students.containsKey(s.getUsername())) {
					students.put(s.getUsername(), s);
					p.addAssignee(s);
				} else {
					p.addAssignee(students.get(s.getUsername()));
				}
			}
		}

		csvParser.close();
	}

	public Map<String, Project> getProjects() {
		return projects;
	}

	public Map<String, Student> getStudents() {
		return students;
	}

}
