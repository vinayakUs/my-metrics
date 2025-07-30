package org.example.client.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {
	
	private String username;
	
	private Date lastSeen;
	
	@Valid
	private List<Item> incomes;

	@Valid
	private List<Item> expenses;

	@Valid
	@NotNull
	private Saving saving;

	@Length(min = 0, max = 20_000)
	private String note;
	

}
