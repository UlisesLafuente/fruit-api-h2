package cat.itacademy.s04.t02.n02.provider.dto;

import cat.itacademy.s04.t02.n02.provider.model.Provider;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDto {
    private Long id;

    @NotBlank(message= "Name is required")
    private String name;

    @NotBlank(message= "Country is required")
    private String country;

    public static ProviderDto fromEntity(Provider provider){
        return new ProviderDto(
                provider.getId(),
                provider.getName(),
                provider.getCountry()
        );
    }

    public Provider toEntity(){
        Provider provider= new Provider();
        provider.setName(this.name);
        provider.setCountry(this.country);
        if(this.id!=null){
            provider.setId(this.id);
        }
        return provider;
    }
}
