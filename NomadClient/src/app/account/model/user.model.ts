export interface User {
  id: number
  firstName:string
  lastName:string
  address:string
  username:string
  password:string
  phoneNumber:string
  userType:string
  suspended:boolean
  roles: string[]
  verified?:boolean
  cancellationNumber?:number
}
