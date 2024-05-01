package fi.haagahelia.quizzer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import fi.haagahelia.quizzer.dto.CreateMessageDto;
import fi.haagahelia.quizzer.model.Message;
import fi.haagahelia.quizzer.repository.MessageRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/messages")
@CrossOrigin(origins = "*")
@Tag(name = "Message", description = "Operations for accessing and managing messages")
public class MessageRestController {
	@Autowired
	private MessageRepository messageRepository;

	@GetMapping("")
	public List<Message> getAllMessages() {
		return messageRepository.findAll();
	}
@Operation(
        summary = "Get a message by id",
        description = "Returns the message with the provided id")

		@ApiResponses(value = {
    // The responseCode property defines the HTTP status code of the response
    @ApiResponse(responseCode = "200", description = "Successful operation"),
    @ApiResponse(responseCode = "404", description = "Message with the provided id does not exist")
})


	@GetMapping("/{id}")
	public Message getMessageById(@PathVariable Long id) {
		return messageRepository.findById(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Message with id " + id + " does not exist"));
	}

	@PostMapping("")
	public Message createMessage(@Valid @RequestBody CreateMessageDto message, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					bindingResult.getAllErrors().get(0).getDefaultMessage());
		}

		Message newMessage = new Message(message.getContent());
		return messageRepository.save(newMessage);
	}
}
