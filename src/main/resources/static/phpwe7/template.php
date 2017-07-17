<?php

global $_W, $_GPC;
$uniacid = $_W['uniacid'];
$op = empty($_GPC['op']) ? 'display' : $_GPC['op'];
$tabname = "{{tablename}}";
if ($op == 'display') {
    $pindex = max(1, intval($_GPC['page']));
    $psize = 15;
    $total = 0;
    $list = pdo_getslice($tabname, array("uniacid"=>$uniacid), array($pindex, $psize), $total);
    $pager = pagination($total, $pindex, $psize);
} elseif ($op == 'post') {
    load()->func('tpl');
    $id = intval($_GPC['id']);
    if ($id > 0) {
        $item = pdo_get($tabname, array('id' => $id, 'uniacid' => $uniacid));
        if (empty($item)) {
            message('项目不存在', referer(), 'info');
        }
    }
    if (checksubmit('submit')) {
//        $name = $_GPC['name'];
//        if (empty($name)) {
//            message("请填写店员姓名", "", "error");
//        }
//        $province = $_GPC['province'];
//        $data = array(
//            'uniacid'=>$uniacid,
//            'name'=>$name,
//            'province'=>$province,
//        );
        {{postdata}}
        if ($id > 0) {
            $ret = pdo_update($tabname, $data,
                array('uniacid'=>$uniacid, 'id'=>$id));
            message('编辑成功', $this->createWebUrl('{{defname}}'), 'success');
        }
        else {
            $ret = pdo_insert($tabname,$data);
            message('添加成功', $this->createWebUrl('{{defname}}'), 'success');
        }
    }
} elseif ($op == 'del') {
    $id = $_GPC['id'];
    $item = pdo_get($tabname, array("id"=>$id));
    if (empty($item)) {
        message('此项不存在或是已经被删除', referer());
    } else {
        pdo_delete($tabname, array('id' => $id, 'uniacid' => $uniacid));
    }
    message('删除成功', referer());
}

include $this->template('{{defname}}');