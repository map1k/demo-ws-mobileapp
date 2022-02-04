package com.ciklum.demowsmobileapp.controller;

import com.ciklum.demowsmobileapp.dto.AddressDto;
import com.ciklum.demowsmobileapp.dto.UserDto;
import com.ciklum.demowsmobileapp.entity.AddressEntity;
import com.ciklum.demowsmobileapp.model.AddressRestModel;
import com.ciklum.demowsmobileapp.model.ErrorMessages;
import com.ciklum.demowsmobileapp.model.UserDetailsRequestModel;
import com.ciklum.demowsmobileapp.model.UserRest;
import com.ciklum.demowsmobileapp.service.AddressService;
import com.ciklum.demowsmobileapp.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.WrongMethodTypeException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    @GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest getUser(@PathVariable String id) {
        UserRest userRest = new UserRest();
        UserDto userDto = userService.getUserByUserId(id);
        BeanUtils.copyProperties(userDto, userRest);
        return userRest;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
                 produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest createUser(@RequestBody UserDetailsRequestModel userDetails) {

        if(userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty())
            throw new WrongMethodTypeException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        ModelMapper modelMapper = new ModelMapper();
        UserDto userDto = modelMapper.map(userDetails, UserDto.class);

        UserDto createdUser = userService.createUser(userDto);
        UserRest userRest = modelMapper.map(createdUser, UserRest.class);
        return userRest;
    }

    @PutMapping(path = "/{id}", consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
               produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public UserRest updateUser(@RequestBody UserDetailsRequestModel userDetails, @PathVariable String id) {
        UserRest userRest = new UserRest();

        if(userDetails.getFirstName() == null || userDetails.getFirstName().isEmpty())
            throw new WrongMethodTypeException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userDetails, userDto);

        UserDto createdUser = userService.updateUser(id, userDto);
        BeanUtils.copyProperties(createdUser, userRest);
        return userRest;
    }

    @DeleteMapping(path = "/{id}")
    public UserRest deleteUser (@PathVariable String id) {
        UserRest userRest = new UserRest();
        UserDto userDto = userService.deleteUserByUserId(id);
        BeanUtils.copyProperties(userDto, userRest);
        return userRest;
    }

    @GetMapping(produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                   @RequestParam(value = "limit", defaultValue = "20") int limit) {
        List<UserRest> returnList = new ArrayList<>();
        List<UserDto> userDtoList = userService.getUsers(page, limit);

        for (UserDto dto : userDtoList) {
            UserRest userModel = new UserRest();
            BeanUtils.copyProperties(dto, userModel);
            returnList.add(userModel);
        }
        return returnList;
    }

    @GetMapping(path = "/{id}/addresses",
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public CollectionModel<AddressRestModel> getUserAddresses(@PathVariable String id) {
        List<AddressRestModel> addressRestModelList = new ArrayList<>();
        List<AddressEntity> addressDtos = addressService.getUserAddresses(id);
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<AddressRestModel>>() {}.getType();
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(id)
                .withRel("user");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(id))
                .withSelfRel();
        addressRestModelList = modelMapper.map(addressDtos, listType);
        for (AddressRestModel addressRestModel : addressRestModelList) {
            Link selfLinkIn = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddress(id, addressRestModel.getAddressId()))
                    .withSelfRel();
            addressRestModel.add(selfLinkIn);
        }
        return CollectionModel.of(addressRestModelList, userLink, selfLink);
    }

    @GetMapping(path = "/{userId}/addresses/{addressId}",
            produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public EntityModel<AddressRestModel> getAddress(@PathVariable String userId, @PathVariable String addressId) {
        AddressDto addressDto = addressService.getUserAddressByAddressId(addressId);

        ModelMapper modelMapper = new ModelMapper();
        AddressRestModel map = modelMapper.map(addressDto, AddressRestModel.class);
        Link userLink = WebMvcLinkBuilder.linkTo(UserController.class)
                .slash(userId)
                .withRel("user");
        Link addressesLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getUserAddresses(userId))
//                .slash(userId)
//                .slash("addresses")
                .withRel("addresses");
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserController.class).getAddress(userId, addressId))
//                .slash(userId)
//                .slash("addresses")
//                .slash(addressId)
                .withSelfRel();

        return EntityModel.of(map, Arrays.asList(userLink, addressesLink, selfLink));
    }
}
