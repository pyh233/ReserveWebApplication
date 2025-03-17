import { createRouter, createWebHistory } from "vue-router";

const routes = [{
    name: "main",
    path: "/main",
    component: () => import("@/components/Main.vue"),
    children: [{
        name: "user",
        path: "/user/msg",
        component: () => import("@/components/User.vue")
    }, {
        name: "userReserve",
        path: "/user/reserve",
        component: () => import("@/components/UserReserve.vue")
    }, {
        name: "expert",
        path: "/expert/msg",
        component: () => import("@/components/Expert.vue")
    }, {
        name: "expertCourse",
        path: "/expert/course",
        component: () => import("@/components/ExpertCourse.vue")
    }, {
        name: "course",
        path: "/course",
        component: () => import("@/components/Course.vue")
    },{
        name:"room",
        path:"/room",
        component:()=>import("@/components/Room.vue")
    },{
        name:"admin",
        path:"/admin",
        component:()=>import("@/components/Admin.vue")
    },{
        name:"adminGroup",
        path:"/admin/group",
        component:()=>import("@/components/AdminGroup.vue")
    },{
        name:"role",
        path:"/role",
        component:()=>import("@/components/Role.vue")
    },{
        name:"permission",
        path:"/permission",
        component:()=>import("@/components/Permission.vue")
    },{
        name:"Route",
        path:"/route",
        component:()=>import("@/components/Route.vue")
    }]
}, {
    name: "login",
    path: "/login",
    component: () => import("@/components/Login.vue")
}, {
    name: "skip-main",
    path: "/",
    redirect: "/main"
}];

const router = createRouter({
    history: createWebHistory(),
    routes
});
export default router;