package org.magnum.dataup;

import org.magnum.dataup.model.Video;
import org.magnum.dataup.model.VideoStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
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
	public @ResponseBody Video saveVideoMetadata(@RequestBody Video video){
		return  videoFileManager.saveVideoMetaData(video);
	}

	@RequestMapping(value = "/video/{id}/data", method = RequestMethod.POST,
			produces = {"application/json; charset=utf-8"} )
	public ResponseEntity<?> saveVideoData(@RequestParam("data") MultipartFile file, @PathVariable String id){

		try {

		Video videoMetadata = videoFileManager.getVideoMetadata(Long.valueOf(id));

		if (videoMetadata == null ) return new ResponseEntity<String>(HttpStatus.NOT_FOUND);


			videoFileManager.saveVideoData(videoMetadata , file.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}


		VideoStatus videostatus = new VideoStatus(VideoStatus.VideoState.READY);

		return new  ResponseEntity(videostatus, HttpStatus.OK);
	}

	@RequestMapping(value = "/video/{id}/data", method = RequestMethod.GET,
			produces = {"application/json; charset=utf-8"})
	public ResponseEntity<?> getVideoData(@PathVariable String id , HttpServletResponse response) {

		Video videoMetadata = videoFileManager.getVideoMetadata(Long.valueOf(id));

		if (videoFileManager.hasVideoData(videoMetadata)){
			try {
				videoFileManager.copyVideoData(videoMetadata, response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
			return new  ResponseEntity(HttpStatus.OK);

	    }else{
			return new  ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}


}
