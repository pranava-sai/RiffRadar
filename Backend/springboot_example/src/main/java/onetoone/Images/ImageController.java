package onetoone.Images;

import io.swagger.v3.oas.annotations.Operation;
import onetoone.Concerts.Concert;
import onetoone.Concerts.ConcertRepository;
import onetoone.Users.Band;
import onetoone.Users.BandRespository;
import onetoone.Users.Venue;
import onetoone.Users.VenueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class ImageController {

    // replace this! careful with the operating system in use
    //private static String directory = "C:\\Users\\yehlp\\mk1_2\\Backend\\springboot_example\\src\\main\\resources\\images";

    private static String directory = "/home/images";
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ConcertRepository concertRepository;

    @Autowired
    BandRespository bandRespository;

    @Autowired
    VenueRepository venueRepository;

    private String success = "{\"message\":\"success\"}";
    private String failure = "{\"message\":\"failure\"}";

    @GetMapping(value = "/images/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    byte[] getImageById(@PathVariable int id) throws IOException {
        Image image = imageRepository.findById(id);
        File imageFile = new File(image.getFilePath());
        return Files.readAllBytes(imageFile.toPath());
    }

    @Operation(summary = "save a new image")
    @PostMapping("/images")
    public int handleFileUpload(@RequestParam("image") MultipartFile imageFile)  {

        try {
            File destinationFile = new File(directory + File.separator + imageFile.getOriginalFilename());
            imageFile.transferTo(destinationFile);  // save file to disk

            Image image = new Image();
            image.setFilePath(destinationFile.getAbsolutePath());
            imageRepository.save(image);

            //return "File uploaded successfully: " + destinationFile.getAbsolutePath();
            return image.getId();
        } catch (IOException e) {
            //return "Failed to upload file: " + e.getMessage();
            return -1;
        }
    }

    @Operation(summary = "Add an image to a concert")
    @PutMapping("/concerts/{concertId}/images/{imageId}")
    String addImageToConcert(@PathVariable int concertId, @PathVariable int imageId){
        Concert concert = concertRepository.findById(concertId);
        Image image = imageRepository.findById(imageId);

        if(image == null || concert == null){
            return failure;
        }
        image.removeConcert();
        concert.removeImage();
        concertRepository.save(concert);
        imageRepository.save(image);

        image.setConcert(concert);
        concert.setActImage(image);
        concertRepository.save(concert);
        imageRepository.save(image);

        return success;
    }

    @Operation(summary = "Delete an Image from a Concert")
    @DeleteMapping("/concerts/{concertId}/images/{imageId}")
    String deleteImageFromConcert(@PathVariable int concertId, @PathVariable int imageId){
        Concert concert = concertRepository.findById(concertId);

        Image image = imageRepository.findById(imageId);

        if(concert == null || image == null){
            return failure;
        }
        concert.removeImage();
        image.removeConcert();

        concertRepository.save(concert);
        imageRepository.save(image);
        return success;
    }

    @Operation(summary = "Add an image to a band")
    @PutMapping("/bands/{bandId}/images/{imageId}")
    String addImageToBand(@PathVariable int bandId, @PathVariable int imageId){
        Band band = bandRespository.findById(bandId);
        Image image = imageRepository.findById(imageId);

        if(image == null || band == null){
            return failure;
        }
        image.removeBand();
        band.removeImage();
        bandRespository.save(band);
        imageRepository.save(image);

        image.setBand(band);
        band.setActImage(image);
        bandRespository.save(band);
        imageRepository.save(image);

        return success;
    }

    @Operation(summary = "Delete an Image from a band")
    @DeleteMapping("/bands/{bandId}/images/{imageId}")
    String deleteImageFromBand(@PathVariable int bandId, @PathVariable int imageId){
        Band band = bandRespository.findById(bandId);

        Image image = imageRepository.findById(imageId);

        if(band == null || image == null){
            return failure;
        }
        band.removeImage();
        image.removeBand();

        bandRespository.save(band);
        imageRepository.save(image);
        return success;
    }

    @Operation(summary = "Add an image to a venue")
    @PutMapping("/venues/{venueId}/images/{imageId}")
    String addImageToVenue(@PathVariable int venueId, @PathVariable int imageId){
        Venue venue = venueRepository.findById(venueId);
        Image image = imageRepository.findById(imageId);

        if(image == null || venue == null){
            return failure;
        }
        image.removeVenue();
        venue.removeImage();
        venueRepository.save(venue);
        imageRepository.save(image);

        image.setVenue(venue);
        venue.setActImage(image);
        venueRepository.save(venue);
        imageRepository.save(image);

        return success;
    }

    @Operation(summary = "Delete an Image from a venue")
    @DeleteMapping("/venues/{venueId}/images/{imageId}")
    String deleteImageFromVenue(@PathVariable int venueId, @PathVariable int imageId){
        Venue venue = venueRepository.findById(venueId);

        Image image = imageRepository.findById(imageId);

        if(venue == null || image == null){
            return failure;
        }
        venue.removeImage();
        image.removeVenue();

        venueRepository.save(venue);
        imageRepository.save(image);
        return success;
    }

}

