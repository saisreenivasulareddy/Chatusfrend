package com.niit.chatusbackend.controller;

import java.util.Date;



import java.util.List;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.chatusbackend.dao.BlogDAO;
import com.niit.chatusbackend.dao.BlogLikesDAO;
import com.niit.chatusbackend.model.Blog;
import com.niit.chatusbackend.model.BlogLikes;
@RestController
public class BlogController {
	private static final Logger logger = 
			LoggerFactory.getLogger(BlogController.class);
	
	@Autowired
	private BlogDAO blogDAO;
	
	@Autowired
	private BlogLikesDAO blogLikesDAO;
	

	@PostMapping(value="/createblog")
	public ResponseEntity<Blog> addblog(@RequestBody Blog blog,HttpSession session){
		logger.debug("calling method create blog");
		System.out.println("hello");
		String name=blog.getTitle();
		System.out.println(name);
		String uid=(String) session.getAttribute("username");
		blog.setStatus("n");
		blog.setDoc(new Date());
		blog.setUserid(uid);
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(blog,HttpStatus.OK);
		
	}
	@GetMapping(value="/blog")
	public ResponseEntity<List<Blog>> listblog(){
		System.out.println("list of blog");
		List<Blog> blog =blogDAO.userlist();
		return new ResponseEntity<List<Blog>>(blog,HttpStatus.OK);
	}
	@GetMapping(value="/adminblog")
	public ResponseEntity<List<Blog>> adminblog(){
		System.out.println("list of blog");
		List<Blog> blog =blogDAO.list();
		return new ResponseEntity<List<Blog>>(blog,HttpStatus.OK);
	}
	@DeleteMapping(value="/deleteblog/{blogid}")
	public ResponseEntity<Blog> deleteblog(Blog blog,@PathVariable("blogid") int blogid){
		Blog blog1=blogDAO.get(blogid);
		blogDAO.delete(blog);
		return new ResponseEntity<Blog>(blog,HttpStatus.OK);
	}
	@PostMapping(value="/likeblog/{blogid}")
	public ResponseEntity<Blog> likeblog(BlogLikes blogLikes,@PathVariable("blogid") int blogid,HttpSession session){
		int uid=(Integer) session.getAttribute("uid");
		blogLikes.setBlogid(blogid);
		blogLikes.setUserid(uid);
		blogLikes.setLikes("like");
		blogLikesDAO.saveOrUpdate(blogLikes);
		List<BlogLikes> list=blogLikesDAO.bloglist(blogid);
		Blog blog=blogDAO.get(blogid);
		blog.setBloglike(list.size());
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}
	@DeleteMapping(value="/unlikeblog/{blogid}")
	public ResponseEntity<Blog> unlike(@PathVariable("blogid") int blogid,HttpSession session){
		int uid=(Integer) session.getAttribute("uid");
		BlogLikes blogLikes=blogLikesDAO.list(uid, blogid);
		blogLikesDAO.delete(blogLikes);
		List<BlogLikes> list=blogLikesDAO.bloglist(blogid);
		Blog blog=blogDAO.get(blogid);
		blog.setBloglike(list.size());
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}
	@PostMapping(value="/acceptblog/{id}")
	public ResponseEntity<Blog> accept(@PathVariable("id") int id){
		Blog blog=blogDAO.get(id);
		blog.setStatus("a");
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);	
	}
	@PostMapping(value="/rejectblog/{id}")
	public ResponseEntity<Blog> reject(@PathVariable("id") int id){
		Blog blog=blogDAO.get(id);
		blog.setStatus("r");
		blogDAO.saveOrUpdate(blog);
		return new ResponseEntity<Blog>(HttpStatus.OK);	
	}
	}
