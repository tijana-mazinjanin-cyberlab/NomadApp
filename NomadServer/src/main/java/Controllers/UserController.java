package Controllers;

import DTO.UserDTO;
import Services.AccommodationService;
import Services.IService;
import Services.ReservationService;
import Services.UserService;
import exceptions.ResourceConflictException;
import model.*;

import model.enums.NotificationType;
import model.enums.UserType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@CrossOrigin(
        origins = {
                "http://localhost:8081"
        },
        methods = {
                RequestMethod.OPTIONS,
                RequestMethod.GET,
                RequestMethod.PUT,
                RequestMethod.DELETE,
                RequestMethod.POST
        })

@RestController
@RequestMapping("/api/users")
@ComponentScan(basePackageClasses = IService.class)
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    ReservationService reservationService;

    @Autowired
    AccommodationService accommodationService;

    @Autowired
    private ModelMapper modelMapper;


    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<UserDTO>> getUsers() {
        Collection<AppUser> appUsers = userService.findAll();
        Collection<UserDTO> userDTOS = appUsers.stream().map(this::convertToDto).toList();
        return new ResponseEntity<Collection<UserDTO>>(userDTOS, HttpStatus.OK);
    }

    //config
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUser(@PathVariable("id") Long id) {
        AppUser appUser = userService.findOne(id);

        if (appUser == null) {
            return new ResponseEntity<UserDTO>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<UserDTO>(this.convertToDto(appUser), HttpStatus.OK);
    }

    //config
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) throws Exception {
        boolean existUser = this.userService.isRegistered(userDTO.getUsername());
        if (existUser) {
            throw new ResourceConflictException(null,"Username already exists");
        }

        AppUser user = null;
        if(userDTO.getRoles().get(0)== UserType.GUEST){
            user = convertToEntityGuest(userDTO);
        } else if(userDTO.getRoles().get(0)== UserType.HOST){
            user = convertToEntityHost(userDTO);
        }else if(userDTO.getRoles().get(0)== UserType.ADMIN){
            user = convertToEntityAdmin(userDTO);
        }else if(userDTO.getRoles().get(0)== UserType.SUPER_ADMIN){
            user = convertToEntitySuperAdmin(userDTO);
        }
        userService.create(user);
        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.CREATED);
    }
    //config
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAccommodation(@PathVariable("id") Long id) {
        AppUser user = userService.findOne(id);
        UserDTO userDto = convertToDto(user);
        switch (userDto.getRoles().get(0)) {
            case HOST:
                if (reservationService.findActiveReservationsForHost(user.getId()).size() > 0) {
                    return new ResponseEntity<String>("This account cannot be deleted, because host has active reservations.", HttpStatus.OK);
                } else {
                    accommodationService.deleteAllForHost(id);
                }
                break;
            case GUEST:
                if (reservationService.findActiveReservationsForGuest(user.getId()).size() > 0) {
                    return new ResponseEntity<String>("This account cannot be deleted, because guest has active reservations.", HttpStatus.OK);
                }
                break;
        }

        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //config
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, @PathVariable Long id) throws Exception {
        AppUser updatedAppUser = switch (userDTO.getRoles().get(0)) {
            case ADMIN -> this.convertToEntityAdmin(userDTO);
            case HOST -> this.convertToEntityHost(userDTO);
            case GUEST -> this.convertToEntityGuest(userDTO);
            default -> this.convertToEntity(userDTO);
        };

        userService.update(updatedAppUser);

        return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/suspend/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> suspendUser(@PathVariable Long id) {
        AppUser user = userService.findOne(id);
        user.setSuspended(true);
        userService.update(user);
        return new ResponseEntity<UserDTO>(HttpStatus.OK);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/un-suspend/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> unsuspentUser(@PathVariable Long id) {
        AppUser user = userService.findOne(id);
        user.setSuspended(false);
        userService.update(user);
        return new ResponseEntity<UserDTO>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @PutMapping(value = "/notifications-preferences/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<NotificationType, Boolean>> manageNotifications(@PathVariable Long id, @RequestParam NotificationType notificationType, @RequestParam boolean value) {
        AppUser user = userService.findOne(id);
        if (user instanceof Host) {
            Host host = (Host) user;
            Map<NotificationType, Boolean> newNotificationPreferences = host.getNotificationPreferences();
            newNotificationPreferences.put(notificationType, value);
            host.setNotificationPreferences(newNotificationPreferences);
            userService.update2(host);
            return new ResponseEntity<Map<NotificationType, Boolean>>(newNotificationPreferences, HttpStatus.OK);
        } else if (user instanceof Guest) {
            Guest guest = (Guest) user;
            Map<NotificationType, Boolean> newNotificationPreferences = guest.getNotificationPreferences();
            newNotificationPreferences.put(notificationType, value);
            guest.setNotificationPreferences(newNotificationPreferences);
            userService.update2(guest);
            return new ResponseEntity<Map<NotificationType, Boolean>>(newNotificationPreferences, HttpStatus.OK);
        }
        return new ResponseEntity<Map<NotificationType, Boolean>>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('HOST') or hasAuthority('GUEST')")
    @GetMapping(value = "/notifications-preferences/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<NotificationType, Boolean>> getNotificationPreferences(@PathVariable("id") Long id) {
        AppUser user = userService.findOne(id);
        if (user instanceof Host) {
            Host host = (Host) user;
            Map<NotificationType, Boolean> notificationPreferences = host.getNotificationPreferences();
            return ResponseEntity.ok(notificationPreferences);

        } else if (user instanceof Guest) {
            Guest guest = (Guest) user;
            Map<NotificationType, Boolean> notificationPreferences = guest.getNotificationPreferences();
            return ResponseEntity.ok(notificationPreferences);
        }
        return null;
    }

    private UserDTO convertToDto(AppUser appUser) {
        ArrayList<UserType> roles = new ArrayList<UserType>();
        UserDTO userDTO = modelMapper.map(appUser, UserDTO.class);
        if(appUser.getClass() == Admin.class){
            roles.add(UserType.ADMIN);
        }else if(appUser.getClass() == Guest.class){
            roles.add(UserType.GUEST);
        }else{
            roles.add(UserType.HOST);
        }
        userDTO.setRoles(roles);

        return userDTO;
    }
    private AppUser convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, AppUser.class);
    }
    private Admin convertToEntityAdmin(UserDTO userDTO) {
        return modelMapper.map(userDTO, Admin.class);
    }
    private Host convertToEntityHost(UserDTO userDTO) {
        return modelMapper.map(userDTO, Host.class);
    }

    private SuperAdmin convertToEntitySuperAdmin(UserDTO userDTO) {
        return modelMapper.map(userDTO, SuperAdmin.class);
    }

    private Guest convertToEntityGuest(UserDTO userDTO) {
        return modelMapper.map(userDTO, Guest.class);
    }

}
