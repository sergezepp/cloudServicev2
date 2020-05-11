package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
public class VideoController {

	@Autowired
	private VideoFileManager videoFileManager;

	@RequestMapping(value = "/video", method = RequestMethod.GET,
			produces = {"application/json; charset=utf-8"})
	public @ResponseBody Collection<Video> getVideosList(){
		return  videoFileManager.getVideosList();
	}

	@RequestMapping(value = "/video", method = RequestMethod.POST,
			produces = {"application/json; charset=utf-8"} , headers = { "Accept=application/json" })
	public @ResponseBody Video getVideosList(@RequestBody Video video){
		return  videoFileManager.saveVideoMetaData(video);
	}
	
}
