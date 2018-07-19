package com.in28minutes.soap.webservices.soapcoursemanagement.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.in28minutes.courses.CourseDetails;
import com.in28minutes.courses.DeleteCourseDetailsRequest;
import com.in28minutes.courses.DeleteCourseDetailsResponse;
import com.in28minutes.courses.GetAllCourseDetailsRequest;
import com.in28minutes.courses.GetAllCourseDetailsResponse;
import com.in28minutes.courses.GetCourseDetailsRequest;
import com.in28minutes.courses.GetCourseDetailsResponse;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.bean.Course;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.exception.CourseNotFoundException;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService;
import com.in28minutes.soap.webservices.soapcoursemanagement.soap.service.CourseDetailsService.Status;

@Endpoint
public class CourseDetailsEndpoint {

	@Autowired
	CourseDetailsService courseDetailsService;
	//method
	//input - GetCourseDetailsRequest
	//output - GetCourseDetailsResponse
	
	@PayloadRoot(namespace="http://in28minutes.com/courses",localPart="GetCourseDetailsRequest")
	@ResponsePayload
	public GetCourseDetailsResponse processCourseDetailsRequest(@RequestPayload GetCourseDetailsRequest courseDetailsRequest) {
		Course course = courseDetailsService.findCoursebyId(courseDetailsRequest.getId());
		if(course == null)
			throw new CourseNotFoundException("Invalid Course Id " + courseDetailsRequest.getId());
		
		return mapCourseDetails(course);
	}

	private GetCourseDetailsResponse mapCourseDetails(Course course) {
		GetCourseDetailsResponse courseDetailsResponse = new GetCourseDetailsResponse();
		courseDetailsResponse.setCourseDetails(mapCourse(course));
		return courseDetailsResponse;
	}
	
	@PayloadRoot(namespace="http://in28minutes.com/courses",localPart="GetAllCourseDetailsRequest")
	@ResponsePayload
	public GetAllCourseDetailsResponse processAllCourseDetailsRequest(@RequestPayload GetAllCourseDetailsRequest courseDetailsRequest) {
		List<Course> courses = courseDetailsService.findAllCourses();
		return mapAllCourseDetails(courses);
	}

	private GetAllCourseDetailsResponse mapAllCourseDetails(List<Course> courses) {
		GetAllCourseDetailsResponse allCourseDetailsResponse = new GetAllCourseDetailsResponse();
		for(Course course: courses) {
			CourseDetails courseDetails = mapCourse(course);
			allCourseDetailsResponse.getCourseDetails().add(courseDetails);
		}
		return allCourseDetailsResponse;
	}
	
	@PayloadRoot(namespace="http://in28minutes.com/courses",localPart="DeleteCourseDetailsRequest")
	@ResponsePayload
	public DeleteCourseDetailsResponse deleteCourseDetailsRequest(@RequestPayload DeleteCourseDetailsRequest deleteCourseDetailsRequest) {
		Status courseStatus = courseDetailsService.deleteById(deleteCourseDetailsRequest.getId()); 
		return deleteCourseDetails(courseStatus);
	}
	
	private DeleteCourseDetailsResponse deleteCourseDetails(Status courseStatus) {
		DeleteCourseDetailsResponse deleteCourseDetailsResponse = new DeleteCourseDetailsResponse();
		deleteCourseDetailsResponse.setStatus(mapStatus(courseStatus));
		return deleteCourseDetailsResponse;
	}
	
	private com.in28minutes.courses.Status mapStatus(Status courseStatus) {
		if(courseStatus==Status.SUCCESS){
			return com.in28minutes.courses.Status.SUCCESS;
		}
		return com.in28minutes.courses.Status.FAILURE;
	}

	private CourseDetails mapCourse(Course course) {
		
		CourseDetails courseDetails = new CourseDetails();
		courseDetails.setId(course.getId());
		courseDetails.setName(course.getName());
		courseDetails.setDescription(course.getDescription());
		
		return courseDetails;
	}
}
