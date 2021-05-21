package uz.pdp.appjparelationships.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentDto {
    private String firstname;
    private String lastname;
    private String city;
    private String district;
    private String street;
    private Integer group_id;
    private Integer subject_id;
}
