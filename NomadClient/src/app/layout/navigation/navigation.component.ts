import {Component, HostListener} from '@angular/core';
import {
  faArrowRightFromBracket,
  faBars,
  faBolt,
  faList,
  faLocation,
  faLocationArrow,
  faMagnifyingGlass,
  faPeopleGroup,
  faPersonWalkingLuggage,
  faSearch,
  faUsers
} from "@fortawesome/free-solid-svg-icons";
import {faEnvelope, faFileLines, faHeart, faPaperPlane, faUser} from "@fortawesome/free-regular-svg-icons";
import {User} from "../../infrastructure/auth/model/user.model";
import {AuthService} from "../../infrastructure/auth/auth.service";
import {Router} from "@angular/router";
import {TokenStorage} from "../../infrastructure/auth/jwt/token.service";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent {
  constructor(private authService: AuthService, private router:Router, private tokenStorage:TokenStorage) {}
  faPersonWalkingLuggage = faPersonWalkingLuggage;
  faHeart = faHeart;
  faEnvelope = faEnvelope;
  faPaper = faPaperPlane;
  faUser = faUser;
  faFileLines = faFileLines;
  faBolt = faBolt;
  faArrowRightFromBracket= faArrowRightFromBracket;
  faList = faList
  faUsers = faUsers;
  faBars = faBars;
  protected readonly faSearch = faSearch;
  protected readonly faLocation = faLocation;
  protected readonly faLocationArrow = faLocationArrow;
  protected readonly faPeopleGroup = faPeopleGroup;
  faMagnifyingGlass= faMagnifyingGlass;
  navBar:boolean=true;
  getScreenWidth: number = 0;
  isHomePage(): boolean {
    return this.router.url === "/home";
  }
  isRequestsPage():boolean{
    return this.router.url === "/reservation-verification" || this.router.url === "/guest-reservation"
  }
  showDropDownMenu(){
    if (this.navBar){
      this.navBar=false;
    }else{
      this.navBar=true;
    }
  }
  user: User ={username: "", id:0, role:""};
  ngOnInit() {
    this.getScreenWidth = window.innerWidth;
    if(this.getScreenWidth<=768){
      this.navBar=false;
    }else {
      this.navBar = true
    }

    this.authService.user$.subscribe(user => {
      this.user = user;
    });
    this.user.role = this.tokenStorage.getRole()||"";
}
  @HostListener('window:resize', ['$event'])
  onWindowResize() {
    this.getScreenWidth = window.innerWidth;

    if(this.getScreenWidth<=768){
      this.navBar=false;
    }else {
      this.navBar = true
    }
  }

  onLogout(): void {
    this.authService.logout();
  }

}
