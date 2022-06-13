//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * @author The one
 * @date 2022-06-13 16:39
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
object Test {

    @JvmStatic
    fun main(args:Array<String>){
        val response = ".m_tv_7I_6,.h_dz_Lm7ei_vPO,.m_lw_jp9Dk_gv,.b_kd_OxPd4_elY{display:inline !important;}.f_os_VVIF_sKCuu,.n_sr_e_zqHsu,.t_mt_U_Il,.t_lv_nAyS_tjr9h{display:none !important;}"
        val validStr = response.replace(".","").run {
            substring(0,indexOf("{"))
        }.split(",")
        print(validStr)
    }

}