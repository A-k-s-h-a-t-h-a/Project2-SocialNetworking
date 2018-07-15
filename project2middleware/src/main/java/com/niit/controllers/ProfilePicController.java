package com.niit.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.niit.dao.ProfilePicDao;
import com.niit.model.ErrorClazz;
import com.niit.model.ProfilePicture;

@RestController
public class ProfilePicController {

	@Autowired
	private ProfilePicDao profilePicDao;

	public ProfilePicController() {
		System.out.println("ProfilePictureController bean is created");
	}

	@RequestMapping(value = "/uploadprofilepic", method = RequestMethod.POST)
	public ResponseEntity<?> uploadProfilePicture(@RequestParam CommonsMultipartFile image, HttpSession session) 
	{
		System.out.println("Profile pic cntrl");
		String email = (String) session.getAttribute("email");
		if (email == null) 
		{
			ErrorClazz error = new ErrorClazz(4, "Unauthrozied access.. Please login");
			return new ResponseEntity<ErrorClazz>(error, HttpStatus.UNAUTHORIZED); 
		}
		ProfilePicture profilePicture = new ProfilePicture();
		profilePicture.setEmail(email);
		profilePicture.setImage(image.getBytes());
		profilePicDao.uploadProfilePicture(profilePicture);

		return new ResponseEntity<ProfilePicture>(profilePicture, HttpStatus.OK);
	}

	@RequestMapping(value = "/getimage/{email:.+}", method = RequestMethod.GET)
	public byte[] getImage(@PathVariable String email, HttpSession session)
	{
		String auth = (String) session.getAttribute("email");
		if (auth == null) {
			return null;
		}
		ProfilePicture profilePicture = profilePicDao.getProfilePicture(email);

		if (profilePicture == null)
			return null;

		return profilePicture.getImage();
	}
}
