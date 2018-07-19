package com.in28minutes.soap.webservices.soapcoursemanagement.soap.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.in28minutes.soap.webservices.soapcoursemanagement.soap.bean.Course;


@Component
public class CourseDetailsService {
	private static List<Course> courses = new ArrayList<>();
	public enum Status{
		SUCCESS, FAILTURE;
	}
	static {
		Course course = new Course(1, "Spring", "10 Steps");
		courses.add(course);
		
		course = new Course(2, "Spring MVC", "10 Examples");
		courses.add(course);
		
		course = new Course(3, "Spring Boot", "6K Students");
		courses.add(course);
		
		course = new Course(4, "Maven", "Most Popular Maven Course.");
		courses.add(course);
	}
	
	public Course findCoursebyId(int id){
		for(Course course: courses) {
			if(course.getId()== id) return course;
		}
		return null;
	}
	
	public List<Course> findAllCourses() {
		return courses;
	}
	
	public Status deleteById(int id) {
		Iterator<Course> iterator = courses.iterator();
		while(iterator.hasNext()) {
			Course course = iterator.next();
			if(course.getId()== id) { 
				iterator.remove();
				return Status.SUCCESS;
			}
		}
		return Status.FAILTURE;
	}
}
